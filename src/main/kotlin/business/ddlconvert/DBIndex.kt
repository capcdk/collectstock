package business.ddlconvert


data class DBIndex(
        var name: String? = "",
        var isUnique: Boolean? = false,
        var fields: HashSet<String> = HashSet(3)
)