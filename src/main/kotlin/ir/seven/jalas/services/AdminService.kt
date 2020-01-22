package ir.seven.jalas.services

interface AdminService {
    fun getSystemGeneralStats(): Map<String, String>
}