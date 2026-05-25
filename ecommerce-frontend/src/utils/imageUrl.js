/**
 * Normalize image URLs to handle different formats:
 * - External URLs (http/https) - return as-is
 * - Backend URLs (contains backend base URL) - extract filename and prefix with /images/
 * - Filenames only - prefix with /images/
 * @param {string} image - The image URL or filename
 * @returns {string} - The normalized image URL
 */
export const getImageUrl = (image) => {
    if (!image) return "/images/default.png"; // Fallback for missing images
    
    // If it's an external URL (http/https), return as-is
    if (image.startsWith("http://") || image.startsWith("https://")) {
        return image;
    }
    
    // If it's a backend URL path (contains /images/), extract just the filename
    if (image.includes("/images/")) {
        const filename = image.split("/images/").pop();
        return `/images/${filename}`;
    }
    
    // Otherwise, assume it's just a filename and prefix with /images/
    return `/images/${image}`;
};
