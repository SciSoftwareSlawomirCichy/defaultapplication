package pl.scisoftware.defaultapplication.actions;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 
 * SystemFileDownload
 * 
 * @author Sławomir Cichy &lt;slawomir.cichy@ibpm.pro&gt;
 * @version $Revision: 1.1 $
 *
 */
@RequestMapping(SystemFileDownloadSupport.MAIN_PATH)
@Controller
public class SystemFileDownload extends SystemFileDownloadSupport implements IFileControllers {

	/**
	 * Pobieranie pliku z serwera (lokalny system plików serwera).
	 * 
	 * "Content-Disposition : attachment" will be directly download, may provide
	 * save as popup, based on your browser setting
	 * 
	 * <pre>
	 * response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));
	 * </pre>
	 * 
	 * @param name     nazwa pliku
	 * @param ext      rozszerzenie pliku
	 * @param request
	 * @param response
	 * @throws MercuryException
	 */
	@RequestMapping(value = DOWNLOAD_SYSTEM_FILE_WITH_SUBDIR_SERVICE, method = RequestMethod.GET)
	public void downloadSystemFile(@PathVariable(SUBDIR_NAME_PARAM) String subdir,
			@PathVariable(FILE_NAME_PARAM) String name, @PathVariable(FILE_EXT_PARAM) String ext,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		downloadSystem(subdir, name, ext, request, response);
	}

	@RequestMapping(value = DOWNLOAD_SYSTEM_FILE_SERVICE, method = RequestMethod.GET)
	public void downloadSystemFile(@PathVariable(FILE_NAME_PARAM) String name, @PathVariable(FILE_EXT_PARAM) String ext,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		downloadSystem(/* subdir */ null, name, ext, request, response);
	}

}
