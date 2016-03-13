import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class JSoupHandler {
	
	public Elements getElements(String source) {
				
	    Document doc = null;
		try {
			doc = Jsoup.connect(source).get();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (doc != null) {
			Elements links = doc.select("body a");
		    return links;			
		}
		else {
			return null;
		}

	}
}