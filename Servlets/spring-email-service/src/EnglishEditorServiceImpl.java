public class EnglishEditorServiceImpl implements EditorService {

	String name = "English editor";
	
	public String composeEmail() {
		return "Hello world.";
	}

	public String getName() {
		return this.name;
	}
}
