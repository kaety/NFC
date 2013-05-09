package datx02.group15.dialogs;


import kandidat.nfc.nfcapp.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;

public class KeysDialogDelete extends DialogFragment {

	@Override
	public AlertDialog onCreateDialog(Bundle savedInstanceState) {

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		builder.setView(inflater.inflate(R.layout.dialog_delete, null));
		builder.setTitle("Delete");
			builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					mListener.onDialogDeletePositiveClick(KeysDialogDelete.this);
				}
			});
			builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					mListener.onDialogDeleteNegativeClick(KeysDialogDelete.this);
				}
			});
		return builder.create();
	}


	public static interface DialogDeleteInterface {
		public void onDialogDeletePositiveClick(DialogFragment dialog);
        public void onDialogDeleteNegativeClick(DialogFragment dialog);
	}
	
	DialogDeleteInterface mListener; 

	public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (DialogDeleteInterface) activity;
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