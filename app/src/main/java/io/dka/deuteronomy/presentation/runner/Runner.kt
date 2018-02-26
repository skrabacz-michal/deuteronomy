package io.dka.deuteronomy.presentation.runner

import arrow.data.Try
import arrow.effects.IO
import arrow.syntax.either.right
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async

interface Runner
{
    fun <A, B> run(
            f: () -> A,
            onError: (Throwable) -> B,
            onSuccess: (A) -> B): IO<B>
}

class BackgroundRunner : Runner
{
    override fun <A, B> run(
            f: () -> A,
            onError: (Throwable) -> B,
            onSuccess: (A) -> B): IO<B>
    {
        return IO.async { process ->
            async(CommonPool) {
                val result = Try { f() }.fold(onError, onSuccess)
                process(result.right())
            }
        }
    }
}

class SingleThreadRunner : Runner
{
    override fun <A, B> run(
            f: () -> A,
            onError: (Throwable) -> B,
            onSuccess: (A) -> B): IO<B>
    {
        return IO.async { process ->
            val result = Try { f() }.fold(onError, onSuccess)
            process(result.right())
        }
    }
}
