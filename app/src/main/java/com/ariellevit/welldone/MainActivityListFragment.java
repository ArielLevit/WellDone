package com.ariellevit.welldone;


import android.app.ListFragment;
import android.os.Bundle;
import java.util.ArrayList;

public class MainActivityListFragment extends ListFragment {

    private ArrayList<Food> foods;
    private FoodAdapter foodAdapter;



    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);


        TimerDbAdapter dbAdapter = new TimerDbAdapter(getActivity().getBaseContext());
        dbAdapter.open();
        foods = dbAdapter.getAllFoods();
        dbAdapter.close();

        foodAdapter = new FoodAdapter(getActivity(), foods);
        setListAdapter(foodAdapter);


        registerForContextMenu(getListView());
    }

//    @Override
//    public void onListItemClick(ListView l, View v, int position, long id){
//        super.onListItemClick(l, v, position, id);
//
//        launchNoteDetailActivity(MainActivity.FragmentToLaunch.VIEW, position);
//    }

//    @Override
//    public void onCreateContextMenu (ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
//        super.onCreateContextMenu(menu, v, menuInfo);
//
//        MenuInflater menuInflater = getActivity().getMenuInflater();
//        menuInflater.inflate(R.menu.long_press_menu, menu);
//    }

//    @Override
//    public boolean onContextItemSelected (MenuItem item){
//
//        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
//        int rowPosition = info.position;
//        Note note = (Note) getListAdapter().getItem(rowPosition);
//        switch (item.getItemId()){
//            case R.id.edit:
//                launchNoteDetailActivity(MainActivity.FragmentToLaunch.EDIT, rowPosition);
//                return true;
//
//            case R.id.delete:
//
//                NotebookDbAdapter dbAdapter = new NotebookDbAdapter(getActivity().getBaseContext());
//                dbAdapter.open();
//                dbAdapter.deleteNote(note.getNoteId());
//                //refresh 1:
////                  notes.remove(rowPosition);
//                //refresh 2:
//                notes.clear();
//                notes.addAll(dbAdapter.getAllNotes());
//                foodAdapter.notifyDataSetChanged();
//                dbAdapter.close();
//                return true;
//        }
//        return onContextItemSelected(item);
//    }


//    private void launchNoteDetailActivity (MainActivity.FragmentToLaunch ftl ,int position) {
//
//        //grab the note information associated with whatever note item we clicked on
//        Note note = (Note) getListAdapter().getItem(position);
//
//        //Crate a new intent that launches our noteDetailActivity
//        Intent intent = new Intent(getActivity(), NoteDetailActivity.class);
//
//        //Pass the information of the note we clicked on to the NoteDetailActivity
//        intent.putExtra(MainActivity.NOTE_ID_EXTRA, note.getNoteId());
//        intent.putExtra(MainActivity.NOTE_TITLE_EXTRA, note.getTitle());
//        intent.putExtra(MainActivity.NOTE_MESSAGE_EXTRA, note.getMessage());
//
//        switch (ftl){
//            case VIEW:
//                intent.putExtra(MainActivity.NOTE_FRAGMENT_TO_LOAD_EXTRA,MainActivity.FragmentToLaunch.VIEW);
//                break;
//            case EDIT:
//                intent.putExtra(MainActivity.NOTE_FRAGMENT_TO_LOAD_EXTRA,MainActivity.FragmentToLaunch.EDIT);
//                break;
//        }
//
//        startActivity(intent);
//    }




}
