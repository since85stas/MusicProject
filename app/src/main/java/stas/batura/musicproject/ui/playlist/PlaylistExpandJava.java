package stas.batura.musicproject.ui.playlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.zip.Inflater;

import stas.batura.musicproject.R;
import stas.batura.musicproject.musicservice.MusicRepository;

import static android.media.CamcorderProfile.get;

public class PlaylistExpandJava extends BaseExpandableListAdapter {

    List<AlbumsDataInfo.AlbumsViewHolder> albums;

    int selAlbId = 0;
    int selTrackId = 0;

    PlaylistExpandJava(List<AlbumsDataInfo.AlbumsViewHolder> albums) {
        this.albums = albums;
        findSelectedTrack();
    }

    private void findSelectedTrack() {

        for (int i = 0; i < albums.size(); i++) {
            for (int j = 0; j < albums.get(i).getAlbumTracks().size(); j++) {
                if (albums.get(i).getAlbumTracks().get(j).isPlaying) {
                    selAlbId = i;
                    selTrackId = j;
                }
            }
        }
    }

    @Override
    public int getGroupCount() {
        return albums.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return albums.get(groupPosition).getAlbumTracks().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return albums.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return albums.get(groupPosition).getAlbumTracks().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.playlist_parent_item_view, null);
        }
        TextView textView = convertView.findViewById(R.id.parent_albums_title_view);
        textView.setText(albums.get(groupPosition).getAlbumName());

        TextView textViewYear = convertView.findViewById(R.id.parent_albums_year);
        textViewYear.setText("" + albums.get(groupPosition).getAlbumYear());
//        if (albums.get(groupPosition).getAlbumYear() == 0) {
//            textViewYear.setVisibility(View.INVISIBLE);
//        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.playlist_child_expand, null);
        }
            TextView textView = convertView.findViewById(R.id.track_child_title);
            textView.setText(albums.get(groupPosition).getAlbumTracks().get(childPosition).title);

            TextView textViewg = convertView.findViewById(R.id.group_test_id);
            textViewg.setText("" + groupPosition);

        TextView textViewc = convertView.findViewById(R.id.child_test_id);
        textViewc.setText("" + childPosition);


            if (albums.get(groupPosition).getAlbumTracks().get(childPosition).isPlaying ) {
                textView.setBackgroundColor(parent.getContext().getResources().getColor(R.color.colorAccent));
            } else {
                textView.setBackgroundColor(parent.getContext().getResources().getColor(R.color.backGroundPrim));
            }
            return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }



    MusicRepository.Track getTrack(int groupPosition, int childPosition) {
        return albums.get(groupPosition).getAlbumTracks().get(childPosition);
    }
}