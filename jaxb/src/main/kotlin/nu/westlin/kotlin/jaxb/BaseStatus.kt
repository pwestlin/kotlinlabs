package nu.westlin.kotlin.jaxb

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlRootElement
import javax.xml.bind.annotation.XmlType

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = ["id"])
abstract class BaseStatus(val id: Int) {
    @Suppress("unused")
    private constructor() : this(id = -1)  // För JAXB
}
