package com; 

public enum Category {
	SPORT,
	PARTY,
	HOBBY,
	HOLIDAYS
	;
	
	public static Category getCategory(String string){
		
		Category cat = null;
		
		if (string.equals(null))
			return null;
		
		switch (string){
		case "sport":
			cat = SPORT;
			break;
		case "party":
			cat = PARTY;
			break;
		case "hobby":
			cat = HOBBY;
			break;
		case "holidays":
			cat = HOLIDAYS;
			break;
		default:
			cat = null;
		}
		
		return cat;
		
	}
	
}
