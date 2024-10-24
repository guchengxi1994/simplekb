const String baseUrl = "http://localhost:8080";
const String streamChatApi = "$baseUrl/kb/llm/chat"; // POST
const String getFileTypesApi = "$baseUrl/kb/file/types"; // GET
const String getFilesApi = "$baseUrl/kb/file/file-with-keywords-by-type"; // GET
const String getPromptsApi = "$baseUrl/kb/prompt/query"; // GET

const String fileUploadByTypePre =
    "$baseUrl/kb/file/upload-by-type/pre"; // POST FORMDATA

const String fileUploadByTypeSubmit =
    "$baseUrl/kb/file/upload-by-type/submit"; // POST 
