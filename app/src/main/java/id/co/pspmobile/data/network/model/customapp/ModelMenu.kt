package id.co.pspmobile.data.network.model.customapp

data class ModelMenu (
    var menu_icon_url: String,
    var menu_svg: String,
    val menu_name: String,
    val translated_menu_name: String,
    val menu_path: String,
    val menu_type: String
)