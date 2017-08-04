object API {
    type SimpleDBIO[+R] = SimpleJdbcAction[R]
    val SimpleDBIO = SimpleJdbcAction

    implicit def queryDeleteActionExtensionMethods[C[_]](q: Query[_ <: RelationalProfile#Table[_], _, C]) (implicit api: API): DeleteActionExtensionMethods =
      api.queryDeleteActionExtensionMethods(q)
    
}
