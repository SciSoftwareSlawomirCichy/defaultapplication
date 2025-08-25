package pl.scisoftware.defaultapplication.actions;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;

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

	protected Logger logger = LoggerFactory.getLogger(getClass());

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
		String baseFileName = String.format("%s.%s", name, ext);
		String mimeType = URLConnection.guessContentTypeFromName(baseFileName);
		if (mimeType == null) {
			mimeType = "application/octet-stream";
		}

		if (logger.isDebugEnabled())
			logger.debug("--> {}: mimetype: {} ", METHOD_NAME, mimeType);

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
			logger.warn("--> {}: Dir '{}' not exists!", METHOD_NAME, targetDir);
			return;
		}
		String serverFileName = dir.getAbsolutePath() + File.separator + baseFileName;
		File serverFile = new File(serverFileName);
		if (!serverFile.exists()) {
			logger.warn("--> {}: File '{}' not exists!", METHOD_NAME, serverFileName);
			return;
		}
		int fileSize = (int) serverFile.length();
		response.setContentLength(fileSize);

		if (logger.isDebugEnabled())
			logger.debug("--> {}: serverFileName: {}, size: {}", METHOD_NAME, serverFileName, fileSize);

		try (InputStream inputStream = new BufferedInputStream(new FileInputStream(serverFile));) {
			FileCopyUtils.copy(inputStream, response.getOutputStream());
		}
	}

}
