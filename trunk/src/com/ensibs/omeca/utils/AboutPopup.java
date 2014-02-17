package com.ensibs.omeca.utils;

import android.app.AlertDialog;
import android.content.Context;

public class AboutPopup {

	public static void show(Context context) {

		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		builder
			.setMessage("OMECA version 2.0 (2013-2014)\n\nRéalisée par:\n\t- Raphaël GICQUIAUX\n\t- Nicolas HALLOUIN\n\t- Sylvain RIO\n\t- Lindsay ROZIER\ndans le cadre du projet de fin d'études de l'ENSIbs")
			.show();

	}

}
