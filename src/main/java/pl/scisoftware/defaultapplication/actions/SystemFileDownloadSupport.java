package pl.scisoftware.defaultapplication.actions;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.FileCopyUtils;

import pl.scisoftware.defaultapplication.utils.TraceUtils;

public abstract class SystemFileDownloadSupport {

	public static final String SCI_FILE_DIR_PROP = "sci.static.file.dir";
	public static final String SCI_FILE_DIR_DEFAULT = "/opt/workspace/static-files";
	public static final String CATALINA_HOME_PROP = "catalina.home";
	public static final String DEFAULT_UPLOAD_DIR = "tmpFiles";

	public static final String SUBDIR_NAME_PARAM = "subdir";
	public static final String FILE_NAME_PARAM = "name";
	public static final String FILE_EXT_PARAM = "ext";
	public static final String FILE_PARAM = "file";

	public static final String MAIN_PATH = "/files";
	public static final String DOWNLOAD_SYSTEM_FILE_SERVICE_CTX = "downloadFile";
	public static final String DOWNLOAD_SYSTEM_FILE_WITH_SUBDIR_SERVICE = "/" + DOWNLOAD_SYSTEM_FILE_SERVICE_CTX
			+ "/{subdir}/{name}.{ext}";
	public static final String DOWNLOAD_SYSTEM_FILE_SERVICE = "/" + DOWNLOAD_SYSTEM_FILE_SERVICE_CTX + "/{name}.{ext}";

	private static final String DISPLAY_CLASSNAME = TraceUtils.createDisplayClazzName(SystemFileDownloadSupport.class);
	private static final Logger trcLogger = TraceUtils
			.createTraceLogger("actions." + SystemFileDownloadSupport.class.getSimpleName());

	protected void downloadSystem(String subdir, String name, String ext, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String targetDir = getSystemTargetDir();
		if (StringUtils.isNotBlank(subdir)) {
			if (subdir.contains(".."))
				throw new IOException("Access to '..' directory is forbidden");
			targetDir = targetDir + "/" + subdir;
		}
		downloadFile(name, ext, request, response, targetDir);
	}

	protected String getSystemTargetDir() {
		String targetDir = System.getProperty(SCI_FILE_DIR_PROP);
		if (StringUtils.isBlank(targetDir)) {
			targetDir = SCI_FILE_DIR_DEFAULT;
		}
		return targetDir;
	}

	protected void downloadFile(String name, String ext, HttpServletRequest request, HttpServletResponse response,
			String targetDir) throws IOException {
		final String METHOD_NAME = DOWNLOAD_SYSTEM_FILE_SERVICE_CTX;
		if (trcLogger.isLoggable(Level.FINE)) {
			trcLogger.entering(DISPLAY_CLASSNAME, METHOD_NAME);
		}

		String baseFileName = String.format("%s.%s", name, ext);
		String mimeType = URLConnection.guessContentTypeFromName(baseFileName);
		if (mimeType == null) {
			mimeType = "application/octet-stream";
		}

		if (trcLogger.isLoggable(Level.FINE)) {
			trcLogger.logp(Level.FINE, DISPLAY_CLASSNAME, METHOD_NAME, String.format("--> mimetype: %s", mimeType));
		}

		response.setContentType(mimeType);
		/*
		 * "Content-Disposition : inline" will show viewable types [like
		 * images/text/pdf/anything viewable by browser] right on browser while
		 * others(zip e.g) will be directly downloaded [may provide save as popup, based
		 * on your browser setting.]
		 */
		response.setHeader("Content-Disposition", String.format("inline; filename=\"%s\"", baseFileName));

		File dir = new File(targetDir);
		if (!dir.exists()) {
			trcLogger.logp(Level.WARNING, DISPLAY_CLASSNAME, METHOD_NAME,
					String.format("--> Dir '%s' not exists.", targetDir));
			response.setStatus(404);
			return;
		}
		String serverFileName = dir.getAbsolutePath() + File.separator + baseFileName;
		File serverFile = new File(serverFileName);
		if (!serverFile.exists()) {
			trcLogger.logp(Level.WARNING, DISPLAY_CLASSNAME, METHOD_NAME,
					String.format("--> File '%s' not exists.", serverFileName));
			response.setStatus(404);
			return;
		}
		int fileSize = (int) serverFile.length();
		response.setContentLength(fileSize);

		if (trcLogger.isLoggable(Level.FINE)) {
			trcLogger.logp(Level.FINE, DISPLAY_CLASSNAME, METHOD_NAME,
					String.format("--> serverFileName: %s, size: %d", serverFileName, fileSize));
		}

		try (InputStream inputStream = new BufferedInputStream(new FileInputStream(serverFile));) {
			FileCopyUtils.copy(inputStream, response.getOutputStream());
		}
		if (trcLogger.isLoggable(Level.FINE)) {
			trcLogger.exiting(DISPLAY_CLASSNAME, METHOD_NAME);
		}
	}

}
