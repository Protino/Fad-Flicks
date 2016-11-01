package com.calgen.prodek.fadflicks.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.calgen.prodek.fadflicks.R;
import com.calgen.prodek.fadflicks.model.Cast;
import com.calgen.prodek.fadflicks.model.Credits;
import com.calgen.prodek.fadflicks.model.Crew;
import com.calgen.prodek.fadflicks.utils.Parser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Gurupad on 07-Sep-16.
 */
public class CreditsAdapter extends RecyclerView.Adapter<CreditsAdapter.CreditsViewHolder> {

    private static final String[] jobsFilter = new String[]{"Director", "Music", "Screenplay"};
    private final Context context;
    private final List<Cast> castList;
    private final List<Crew> crewList;
    private final List<Object> cards;

    public CreditsAdapter(Context context, Credits credits) {
        this.context = context;
        this.castList = credits.getCast();
        this.crewList = credits.getCrew();
        cards = new ArrayList<>();

        initializeDataForCards();
    }

    private static boolean hasRequiredJob(String job) {
        for (String s : jobsFilter) {
            if (job.contains(s))
                return true;
        }
        return false;
    }

    private void initializeDataForCards() {
        //fetch only 5 actors with profile path(so we know that they are famous)
        //fetch director , screenplay artist or photographer and music director
        short count = 0;
        for (int i = 0; i < castList.size() && count < 6; i++) {
            Cast cast = castList.get(i);
            if (cast.getProfilePath() != null) {
                cards.add(cast);
                count++;
            }
        }
        count = 0;

        for (int i = 0; i < crewList.size() && count < 6; i++) {
            Crew crew = crewList.get(i);
            if (hasRequiredJob(crew.getJob())) {
                cards.add(crew);
                count++;
            }
        }
    }

    @Override
    public CreditsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_movie_credits, parent, false);
        return new CreditsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CreditsViewHolder holder, int position) {
        Object object = cards.get(position);
        String name;
        String role;
        String posterPath;
        if (object instanceof Cast) {
            Cast cast = (Cast) object;
            name = cast.getName();
            role = cast.getCharacter();
            Object profilePath = cast.getProfilePath();
            posterPath = profilePath == null ? "" : (String) profilePath;
        } else {
            Crew crew = (Crew) object;
            name = crew.getName();
            role = crew.getJob();
            Object profilePath = crew.getProfilePath();
            posterPath = profilePath == null ? "" : (String) profilePath;
        }

        posterPath = Parser.formatImageUrl(posterPath, context.getString(R.string.image_size_small));

        holder.name.setText(name);
        holder.role.setText(role);
        Picasso.with(context)
                .load(posterPath)
                .placeholder(R.mipmap.ic_profile).error(R.mipmap.ic_profile)
                .into(holder.poster);
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    class CreditsViewHolder extends RecyclerView.ViewHolder {

        //@formatter:off
        @BindView(R.id.poster) public ImageView poster;
        @BindView(R.id.name) public TextView name;
        @BindView(R.id.role) public TextView role;
        //@formatter:on

        CreditsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.poster, R.id.name, R.id.role})
        public void onClick() {
            // TODO: 30-Nov-16 show more details
        }
    }
}
