package nu.westlin.kotlin.jaxb

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlRootElement
import javax.xml.bind.annotation.XmlType

@XmlRootElement(name = "Status")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = ["id", "status"])
class Status(id: Int, val status: String): BaseStatus(id) {
    @Suppress("unused")
    private constructor() : this(id = -1, status = "")  // För JAXB
}
