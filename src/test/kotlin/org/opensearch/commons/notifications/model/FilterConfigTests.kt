/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package org.opensearch.commons.notifications.model

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.opensearch.commons.utils.createObjectFromJsonString
import org.opensearch.commons.utils.getJsonString
import org.opensearch.commons.utils.recreateObject

internal class FilterConfigTests {

    @Test
    fun `Config serialize and deserialize with default isEnabled flag should be equal`() {
        val sampleConfig = FeatureChannel(
            "config_id",
            "name",
            "description",
            ConfigType.SLACK
        )
        val recreatedObject = recreateObject(sampleConfig) { FeatureChannel(it) }
        assertEquals(sampleConfig, recreatedObject)
    }

    @Test
    fun `Config serialize and deserialize with isEnabled=false should be equal`() {
        val sampleConfig = FeatureChannel(
            "config_id",
            "name",
            "description",
            ConfigType.CHIME,
            false
        )
        val recreatedObject = recreateObject(sampleConfig) { FeatureChannel(it) }
        assertEquals(sampleConfig, recreatedObject)
    }

    @Test
    fun `Config serialize and deserialize using json object with default isEnabled flag should be equal`() {
        val sampleConfig = FeatureChannel(
            "config_id",
            "name",
            "description",
            ConfigType.WEBHOOK
        )
        val jsonString = getJsonString(sampleConfig)
        val recreatedObject = createObjectFromJsonString(jsonString) { FeatureChannel.parse(it) }
        assertEquals(sampleConfig, recreatedObject)
    }

    @Test
    fun `Config serialize and deserialize using json object with isEnabled=false should be equal`() {
        val sampleConfig = FeatureChannel(
            "config_id",
            "name",
            "description",
            ConfigType.EMAIL_GROUP,
            false
        )
        val jsonString = getJsonString(sampleConfig)
        val recreatedObject = createObjectFromJsonString(jsonString) { FeatureChannel.parse(it) }
        assertEquals(sampleConfig, recreatedObject)
    }

    @Test
    fun `Config should safely ignore extra field in json object`() {
        val sampleConfig = FeatureChannel(
            "config_id",
            "name",
            "description",
            ConfigType.EMAIL
        )
        val jsonString = """
        {
            "config_id":"config_id",
            "name":"name",
            "description":"description",
            "config_type":"email",
            "is_enabled":true,
            "extra_field_1":["extra", "value"],
            "extra_field_2":{"extra":"value"},
            "extra_field_3":"extra value 3"
        }
        """.trimIndent()
        val recreatedObject = createObjectFromJsonString(jsonString) { FeatureChannel.parse(it) }
        assertEquals(sampleConfig, recreatedObject)
    }

    @Test
    fun `Config should safely ignore unknown config type in json object`() {
        val sampleConfig = FeatureChannel(
            "config_id",
            "name",
            "description",
            ConfigType.NONE
        )
        val jsonString = """
        {
            "config_id":"config_id",
            "name":"name",
            "description":"description",
            "config_type":"NewConfig"
        }
        """.trimIndent()
        val recreatedObject = createObjectFromJsonString(jsonString) { FeatureChannel.parse(it) }
        assertEquals(sampleConfig, recreatedObject)
    }

    @Test
    fun `Config throw exception if configId is empty`() {
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            FeatureChannel(
                "",
                "name",
                "description",
                ConfigType.EMAIL_GROUP
            )
        }
    }

    @Test
    fun `Config throw exception if name is empty`() {
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            FeatureChannel(
                "config_id",
                "",
                "description",
                ConfigType.EMAIL_GROUP
            )
        }
    }
}
