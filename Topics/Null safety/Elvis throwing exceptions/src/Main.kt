fun main() {
    val age: Int? = null
    val name: String? = "Bob"
    val nickname: String? = null
    val length: Int = nickname?.length ?: 0
    println("$age ${name?.length} $length")
}