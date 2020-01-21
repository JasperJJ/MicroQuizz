package com.jasper.microquizz;

import android.app.Application;
import com.jasper.microquizz.models.Museums;

public class App extends Application {
	private Museums museum;

	public Museums getMuseum() {
		return museum;
	}

	public void setMuseum(Museums museum) {
		this.museum = museum;
	}
}
