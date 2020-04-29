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

    PlaylistExpandJava(List<AlbumsDataInfo.AlbumsViewHolder> albums) {
        this.albums = albums;
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
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.playlist_parent_item_view, null);
        }
        TextView textView = convertView.findViewById(R.id.parent_albums_title_view);
        textView.setText(albums.get(groupPosition).getAlbumName());
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
            if (albums.get(groupPosition).getAlbumTracks().get(childPosition).isPlaying ) {
                textView.setBackgroundColor(parent.getContext().getResources().getColor(R.color.colorAccent));
            }
            return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }



    MusicRepository.Track getTrack(int groupPosition, int childPosition) {
        return albums.get(groupPosition).getAlbumTracks().get(childPosition);
    }
}
