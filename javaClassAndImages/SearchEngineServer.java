package mainCode;

public class SearchEngineServer {
	static String returnKeySearch(String key) {
		switch (key) {
		case "apple":
			return "red, apple shaped, keeps doctor away";
		case "boy":
			return "special kind of species who disguise themselves as humans";
		case "cat":
			return "small, long, colour varies, soft and funny,says meow";
		case "dog":
			return "medium size, cute, colour varies, barks, scares some people";
		case "egg":
			return "oval, white, may or may not be eaten";
		case "fish":
			return "lives in water, breathes in water, colourful, wide variety";
		case "girl":
			return "intelligent human being, sweet and nice";
		case "house":
			return "where humans live, animals may also live here";

		default:
			return "Sorry, no search result available";
		}
	}
}
