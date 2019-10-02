import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class EmailController {

	private EditorService editorService;
	
	@ResponseBody
    @RequestMapping(value = "/")
    public String helloWorld()
    {
        return "Hello world 123!";
    }
	
	@ResponseBody
	@RequestMapping(value = "/compose")
	public String getComposedEmail() {
		return editorService.composeEmail();
	}	
	
	@ResponseBody
    @RequestMapping(value = "/", params = {"action"}, method=RequestMethod.GET)
    public String getGreeting(@RequestParam("action") String action)
    {
		String ret = "";
		if (action != null && (action.equalsIgnoreCase("compose") ||
				action.equalsIgnoreCase("create"))) {
			
			//
			//
			///
			ret = editorService.composeEmail();
			editorService.thisIsVoidFunction();
		}
		return ret;
    }
	
	@ResponseBody
    @RequestMapping(value = "/", params = {"action"}, method=RequestMethod.GET)
    public String getGreeting1(@RequestParam("action") String action)
    {
		String ret = "";
		if (action.equalsIgnoreCase("compose") ||
				action.equalsIgnoreCase("create") &&
				editorService.getName().equals("EnglishEditor")) {
			
			//
			//
			//
			
			ret = editorService.composeEmail();
			editorService.thisIsVoidFunction();
		}
		return ret;
    }

	
	public String getWithGhostEditor() {
		editorService = new EnglishEditorServiceImpl();
        return editorService.composeEmail();		
	}
	
	public void setEditorService(EditorService editorService) {
		this.editorService = editorService;
	}
}
