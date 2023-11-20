package id.co.pspmobile.data.network.responses.customapp

data class AppMenu(
    val id: String,
    val menu_icon_url: String,
    val menu_is_show: Boolean,
    val menu_name: String,
    val menu_path: String,
    val menu_type: String
)