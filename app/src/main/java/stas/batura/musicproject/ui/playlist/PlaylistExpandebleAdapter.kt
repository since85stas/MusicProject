package stas.batura.musicproject.ui.playlist

import android.content.Context
import android.database.DataSetObserver
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListAdapter
import android.widget.TextView
import stas.batura.musicproject.R

class PlaylistExpandebleAdapter : ExpandableListAdapter  {

    private lateinit var albums: List<AlbumsDataInfo.AlbumsViewHolder>

    private lateinit var contex: Context

    constructor(albums: List<AlbumsDataInfo.AlbumsViewHolder>, contex: Context) {
        this.albums = albums
        this.contex = contex
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return albums.get(groupPosition).albumTracks!!.size
    }

    override fun getGroup(groupPosition: Int): Any {
        return albums.get(groupPosition)
    }

    override fun onGroupCollapsed(groupPosition: Int) {
    }

    override fun isEmpty(): Boolean {
        return  albums.size == 0
    }

    override fun registerDataSetObserver(observer: DataSetObserver?) {
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return albums.get(groupPosition).albumTracks!!.get(childPosition)
    }

    override fun onGroupExpanded(groupPosition: Int) {
    }

    override fun getCombinedChildId(groupId: Long, childId: Long): Long {
        return 0
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {

        if (convertView == null) {
            var view: View? = null
            val inflater = contex.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.playlist_child_expand, null)
            view.findViewById<TextView>(R.id.track_child_title).text =
                albums.get(groupPosition).albumTracks!!.get(childPosition).title
            return view;
        } else {
            return convertView
        }

    }

    override fun areAllItemsEnabled(): Boolean {
        return true
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getCombinedGroupId(groupId: Long): Long {
        return 0
    }

    override fun getGroupView(
        groupPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        if (convertView == null) {
            var view: View? = null
            val inflater = contex.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.playlist_parent_item_view, null)
            view.findViewById<TextView>(R.id.parent_albums_title_view).text =
                albums.get(groupPosition).albumName
            return view;
        } else {
            return convertView
        }
    }

    override fun unregisterDataSetObserver(observer: DataSetObserver?) {
    }

    override fun getGroupCount(): Int {
        return albums.size
    }
}