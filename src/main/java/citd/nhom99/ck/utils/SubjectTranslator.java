package citd.nhom99.ck.utils;

public class SubjectTranslator {
    
    /**
     * Convert English subject names to Vietnamese
     * @param subjectName The subject name in English
     * @return The subject name in Vietnamese
     */
    public static String convertToVietnamese(String subjectName) {
        if (subjectName == null || subjectName.trim().isEmpty()) {
            return "Chưa phân môn";
        }
        
        // Convert to lowercase for case-insensitive matching
        String lowerSubject = subjectName.toLowerCase().trim();
        
        return switch (lowerSubject) {
            case "math", "mathematics" -> "Toán học";
            case "physics" -> "Vật lý";
            case "chemistry" -> "Hóa học";
            case "biology" -> "Sinh học";
            case "geography" -> "Địa lý";
            case "literature" -> "Ngữ văn";
            case "foreign language", "english" -> "Ngoại ngữ";
            case "history" -> "Lịch sử";
            case "civic education", "civics" -> "Giáo dục công dân";
            case "physical education", "pe" -> "Thể dục";
            case "art" -> "Mỹ thuật";
            case "music" -> "Âm nhạc";
            case "computer science", "informatics" -> "Tin học";
            case "technology" -> "Công nghệ";
            default -> subjectName; // Return original if no match found
        };
    }
}
