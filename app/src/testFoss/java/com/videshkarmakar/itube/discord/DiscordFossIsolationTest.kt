package com.videshkarmakar.itube.discord

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class DiscordFossIsolationTest {
    @Test
    fun `foss classpath excludes functional Discord implementation`() {
        val forbiddenClasses = listOf(
            "com.videshkarmakar.itube.discord.DiscordTokenStore",
            "com.videshkarmakar.itube.discord.DiscordAuthTokens",
            "com.videshkarmakar.itube.discord.DiscordPlaybackSource",
            "com.videshkarmakar.itube.discord.DiscordPresenceCoordinator",
            "com.videshkarmakar.itube.discord.KizzyDiscordPresenceTransport",
            "com.videshkarmakar.itube.discord.KizzyGatewayProtocol",
        )

        forbiddenClasses.forEach { className ->
            assertThat(runCatching { Class.forName(className) }.isFailure).isTrue()
        }
    }

    @Test
    fun `foss runtime reports Discord unavailable`() {
        assertThat(DiscordPresenceRuntime.settingsState.value.isAvailable).isFalse()
        assertThat(DiscordPresenceRuntime.settingsState.value.isEnabled).isFalse()
        assertThat(DiscordPresenceRuntime.settingsState.value.summary)
            .isEqualTo(DiscordSettingsSummary.UNAVAILABLE)
    }
}
