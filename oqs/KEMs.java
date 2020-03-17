package oqs;

import java.util.ArrayList;

/**
 * \class oqs::KEMs
 * \brief Singleton class, contains details about supported/enabled key exchange
 * mechanisms (KEMs)
 */
public class KEMs {
    
    static { System.loadLibrary("oqs-jni"); }

    /**
     * Wrapper for OQS_API int OQS_KEM_alg_count(void);
     *
     * \brief Maximum number of supported KEM algorithms
     * \return Maximum number of supported KEM algorithms
     */
    public static native int max_number_KEMs();

    /**
     * Wrapper for OQS_API int OQS_KEM_alg_is_enabled(const char *method_name);
     *
     * \brief Checks whether the KEM algorithm \a alg_name is enabled
     * \param alg_name Cryptographic algorithm name
     * \return True if the KEM algorithm is enabled, false otherwise
     */
    public static native boolean is_KEM_enabled(String alg_name);

    /**
     * Wrapper for OQS_API const char *OQS_KEM_alg_identifier(size_t i);
     * 
     * \brief KEM algorithm name
     * \param alg_id Cryptographic algorithm numerical id
     * \return KEM algorithm name
     */
    public static native String get_KEM_name(long alg_id);


    /**
     * \brief ArrayList of supported KEM algorithms
     * \return ArrayList of supported KEM algorithms
     */
    public static ArrayList<String> get_supported_KEMs() {
        ArrayList<String> supported_KEMs = new ArrayList<>();
        for (int i = 0; i < max_number_KEMs(); ++i) {
            supported_KEMs.add(get_KEM_name(i));
        }
        return supported_KEMs;
    }
    
    /**
     * \brief Vector of enabled KEM algorithms
     * \return Vector of enabled KEM algorithms
     */
    public static ArrayList<String> get_enabled_KEMs() {
        ArrayList<String> enabled_KEMs = new ArrayList<>();
        for (String elem : get_supported_KEMs()) {
            if (is_KEM_enabled(elem)) {
                enabled_KEMs.add(elem);
            }
        }
        return enabled_KEMs;
    }
    
    /**
     * \brief Checks whether the KEM algorithm \a alg_name is supported
     * \param alg_name Cryptographic algorithm name
     * \return True if the KEM algorithm is supported, false otherwise
     */
    public static boolean is_KEM_supported(String alg_name) {
        ArrayList<String> supported_KEMs = get_supported_KEMs();
        return supported_KEMs.contains(alg_name);
    }
    
}
