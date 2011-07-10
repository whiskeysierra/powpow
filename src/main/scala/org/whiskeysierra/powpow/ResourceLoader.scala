package org.whiskeysierra.powpow

import com.google.common.io.Resources
import java.io.InputStream

trait ResourceLoader {

    def load(fileName: String): InputStream = Resources.getResource(fileName).openStream

}