package server;

import java.io.Serializable;

public enum ProjectState implements Serializable{
	COMPLETED,
	ACTIVE,
	INACTIVE;
	
	public static ProjectState getState(String string){
		
		ProjectState state = null;
		
		if (string.equals(null))
			return null;
		
		switch (string){
		case "inactive":
			state = INACTIVE;
			break;
		case "active":
			state = ACTIVE;
			break;
		case "completed":
			state = COMPLETED;
			break;
		default:
			state = null;
		}	
		return state;		
	}
}


