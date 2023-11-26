package id.co.pspmobile.data.network.attendance

data class AttendanceResDto(
    var name: String,
    var bulan: Int,
    var tahun: Int,
    var hari: Int,
    var shift_name: String? = null,
    var nis: String,
    var tanggal_absen: String,
    var tanggal_absen_str: String,
    var jam_absen_masuk: String,
    var jam_absen_pulang: String,
    var sesi_ket_masuk: String,
    var sesi_ket_pulang: String,
    var photo_url_masuk: String,
    var photo_url_pulang: String
)
