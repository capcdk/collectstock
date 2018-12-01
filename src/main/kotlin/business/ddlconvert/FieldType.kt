package business.ddlconvert

class FieldType(type: String) {

    var name: String = type
    var code: String = type
    var apply: Apply? = Apply(type)

}

class Apply(type: String) {
    var MYSQL: MYSQL? = MYSQL(type)
}

class MYSQL(
        var type: String? = ""
)