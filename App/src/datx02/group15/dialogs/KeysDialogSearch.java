package datx02.group15.dialogs;

import kandidat.nfc.nfcapp.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class KeysDialogSearch extends DialogFragment {

	private EditText doorId;

	@Override
	public AlertDialog onCreateDialog(Bundle savedInstanceState) {

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.dialog_search, null);	

		doorId = (EditText) view.findViewById(R.id.dialog_door_id);
		
		builder.setView(view);
		
		builder.setPositiveButton("Search", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				String door = doorId.getText().toString();
				mListener.onDialogSearchPositiveClick(KeysDialogSearch.this, door);
			}
		});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				mListener.onDialogSearchNegativeClick(KeysDialogSearch.this);
			}
		});
		return builder.create();
	}

	public static interface DialogSearchInterface {
		public void onDialogSearchPositiveClick(DialogFragment dialog, String door);
		public void onDialogSearchNegativeClick(DialogFragment dialog);
	}

	DialogSearchInterface mListener; 

	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// Verify that the host activity implements the callback interface
		try {
			// Instantiate the NoticeDialogListener so we can send events to the host
			mListener = (DialogSearchInterface) activity;
		} catch (ClassCastException e) {
			// The activity doesn't implement the interface, throw exception
			throw new ClassCastException(activity.toString()
					+ " must implement NoticeDialogListener");
		}
	}

	@Override
	public void onDetach() {
		mListener = null;
		super.onDetach();
	}

}