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

public class KeysDialogCreate extends DialogFragment {

	private EditText doorId,doorKey;
	
	@Override
	public AlertDialog onCreateDialog(Bundle savedInstanceState) {

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.dialog_create, null);	
		
		doorId = (EditText) view.findViewById(R.id.dialog_door_id);
		doorKey = (EditText) view.findViewById(R.id.dialog_door_key);
		
		builder.setView(view);

		builder.setTitle("Create");
		
		builder.setPositiveButton(R.string.dialog_create, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				String door = doorId.getText().toString();
				String key = doorKey.getText().toString();
				mListener.onDialogCreatePositiveClick(KeysDialogCreate.this, door, key);
			}
		});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				mListener.onDialogCreateNegativeClick(KeysDialogCreate.this);
			}
		});
		return builder.create();
	}
	
	public static interface DialogCreateInterface {
		public void onDialogCreatePositiveClick(DialogFragment dialog, String door, String key);
        public void onDialogCreateNegativeClick(DialogFragment dialog);
	}
	
	DialogCreateInterface mListener; 

	public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (DialogCreateInterface) activity;
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