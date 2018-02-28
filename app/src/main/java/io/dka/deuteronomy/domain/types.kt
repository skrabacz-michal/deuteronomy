package io.dka.deuteronomy.domain

import arrow.HK
import arrow.data.Reader
import arrow.effects.IOHK

typealias AsyncResult<D, A> = Reader<D, HK<IOHK, A>>
