package com.example.shramsarthi.ui.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

object ShramIcons {
    val ArrowBack: ImageVector by lazy {
        ImageVector.Builder(
            name = "ArrowBack",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFF1E293B))) {
                moveTo(20f, 11f)
                horizontalLineTo(7.83f)
                lineTo(13.42f, 5.41f)
                lineTo(12f, 4f)
                lineTo(4f, 12f)
                lineTo(12f, 20f)
                lineTo(13.41f, 18.59f)
                lineTo(7.83f, 13f)
                horizontalLineTo(20f)
                close()
            }
        }.build()
    }

    val ArrowForward: ImageVector by lazy {
        ImageVector.Builder(
            name = "ArrowForward",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFF1E293B))) {
                moveTo(12f, 4f)
                lineTo(10.59f, 5.41f)
                lineTo(16.17f, 11f)
                horizontalLineTo(4f)
                verticalLineTo(13f)
                horizontalLineTo(16.17f)
                lineTo(10.59f, 18.59f)
                lineTo(12f, 20f)
                lineTo(20f, 12f)
                close()
            }
        }.build()
    }

    val Check: ImageVector by lazy {
        ImageVector.Builder(
            name = "Check",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFF1E293B))) {
                moveTo(9f, 16.17f)
                lineTo(4.83f, 12f)
                lineTo(3.41f, 13.41f)
                lineTo(9f, 19f)
                lineTo(21f, 7f)
                lineTo(19.59f, 5.59f)
                close()
            }
        }.build()
    }

    val CheckCircle: ImageVector by lazy {
        ImageVector.Builder(
            name = "CheckCircle",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFF1E293B))) {
                moveTo(12f, 2f)
                curveTo(6.48f, 2f, 2f, 6.48f, 2f, 12f)
                curveTo(2f, 17.52f, 6.48f, 22f, 12f, 22f)
                curveTo(17.52f, 22f, 22f, 17.52f, 22f, 12f)
                curveTo(22f, 6.48f, 17.52f, 2f, 12f, 2f)
                close()
                moveTo(10f, 17f)
                lineTo(5f, 12f)
                lineTo(6.41f, 10.59f)
                lineTo(10f, 14.17f)
                lineTo(17.59f, 6.58f)
                lineTo(19f, 8f)
                close()
            }
        }.build()
    }

    val Person: ImageVector by lazy {
        ImageVector.Builder(
            name = "Person",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFF1E293B))) {
                moveTo(12f, 12f)
                curveTo(14.21f, 12f, 16f, 10.21f, 16f, 8f)
                curveTo(16f, 5.79f, 14.21f, 4f, 12f, 4f)
                curveTo(9.79f, 4f, 8f, 5.79f, 8f, 8f)
                curveTo(8f, 10.21f, 9.79f, 12f, 12f, 12f)
                close()
                moveTo(12f, 14f)
                curveTo(9.33f, 14f, 4f, 15.34f, 4f, 18f)
                verticalLineTo(20f)
                horizontalLineTo(20f)
                verticalLineTo(18f)
                curveTo(20f, 15.34f, 14.67f, 14f, 12f, 14f)
                close()
            }
        }.build()
    }

    val Phone: ImageVector by lazy {
        ImageVector.Builder(
            name = "Phone",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFF1E293B))) {
                moveTo(6.62f, 10.79f)
                curveTo(8.06f, 13.62f, 10.38f, 15.94f, 13.21f, 17.38f)
                lineTo(15.41f, 15.18f)
                curveTo(15.69f, 14.9f, 16.08f, 14.82f, 16.43f, 14.94f)
                curveTo(17.55f, 15.31f, 18.76f, 15.51f, 20f, 15.51f)
                curveTo(20.55f, 15.51f, 21f, 15.96f, 21f, 16.51f)
                verticalLineTo(20f)
                curveTo(21f, 20.55f, 20.55f, 21f, 20f, 21f)
                curveTo(10.61f, 21f, 3f, 13.39f, 3f, 4f)
                curveTo(3f, 3.45f, 3.45f, 3f, 4f, 3f)
                horizontalLineTo(7.5f)
                curveTo(8.05f, 3f, 8.5f, 3.45f, 8.5f, 4f)
                curveTo(8.5f, 5.25f, 8.7f, 6.45f, 9.07f, 7.57f)
                curveTo(9.18f, 7.92f, 9.1f, 8.31f, 8.82f, 8.59f)
                close()
            }
        }.build()
    }

    val Lock: ImageVector by lazy {
        ImageVector.Builder(
            name = "Lock",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFF1E293B))) {
                moveTo(18f, 8f)
                horizontalLineTo(17f)
                verticalLineTo(6f)
                curveTo(17f, 3.24f, 14.76f, 1f, 12f, 1f)
                curveTo(9.24f, 1f, 7f, 3.24f, 7f, 6f)
                verticalLineTo(8f)
                horizontalLineTo(6f)
                curveTo(4.9f, 8f, 4f, 8.9f, 4f, 10f)
                verticalLineTo(20f)
                curveTo(4f, 21.1f, 4.9f, 22f, 6f, 22f)
                horizontalLineTo(18f)
                curveTo(19.1f, 22f, 20f, 21.1f, 20f, 20f)
                verticalLineTo(10f)
                curveTo(20f, 8.9f, 19.1f, 8f, 18f, 8f)
                close()
                moveTo(9f, 6f)
                curveTo(9f, 4.34f, 10.34f, 3f, 12f, 3f)
                curveTo(13.66f, 3f, 15f, 4.34f, 15f, 6f)
                verticalLineTo(8f)
                horizontalLineTo(9f)
                close()
                moveTo(12f, 17f)
                curveTo(10.9f, 17f, 10f, 16.1f, 10f, 15f)
                curveTo(10f, 13.9f, 10.9f, 13f, 12f, 13f)
                curveTo(13.1f, 13f, 14f, 13.9f, 14f, 15f)
                curveTo(14f, 16.1f, 13.1f, 17f, 12f, 17f)
                close()
            }
        }.build()
    }

    val Star: ImageVector by lazy {
        ImageVector.Builder(
            name = "Star",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFF1E293B))) {
                moveTo(12f, 17.27f)
                lineTo(18.18f, 21f)
                lineTo(16.54f, 13.97f)
                lineTo(22f, 9.24f)
                lineTo(14.81f, 8.63f)
                lineTo(12f, 2f)
                lineTo(9.19f, 8.63f)
                lineTo(2f, 9.24f)
                lineTo(7.46f, 13.97f)
                lineTo(5.82f, 21f)
                close()
            }
        }.build()
    }

    val Info: ImageVector by lazy {
        ImageVector.Builder(
            name = "Info",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFF1E293B))) {
                moveTo(12f, 2f)
                curveTo(6.48f, 2f, 2f, 6.48f, 2f, 12f)
                curveTo(2f, 17.52f, 6.48f, 22f, 12f, 22f)
                curveTo(17.52f, 22f, 22f, 17.52f, 22f, 12f)
                curveTo(22f, 6.48f, 17.52f, 2f, 12f, 2f)
                close()
                moveTo(13f, 17f)
                horizontalLineTo(11f)
                verticalLineTo(11f)
                horizontalLineTo(13f)
                close()
                moveTo(13f, 9f)
                horizontalLineTo(11f)
                verticalLineTo(7f)
                horizontalLineTo(13f)
                close()
            }
        }.build()
    }

    val LocationPin: ImageVector by lazy {
        ImageVector.Builder(
            name = "LocationPin",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFF1E293B))) {
                moveTo(12f, 2f)
                curveTo(8.13f, 2f, 5f, 5.13f, 5f, 9f)
                curveTo(5f, 14.25f, 12f, 22f, 12f, 22f)
                curveTo(12f, 22f, 19f, 14.25f, 19f, 9f)
                curveTo(19f, 5.13f, 15.87f, 2f, 12f, 2f)
                close()
                moveTo(12f, 11.5f)
                curveTo(10.62f, 11.5f, 9.5f, 10.38f, 9.5f, 9f)
                curveTo(9.5f, 7.62f, 10.62f, 6.5f, 12f, 6.5f)
                curveTo(13.38f, 6.5f, 14.5f, 7.62f, 14.5f, 9f)
                curveTo(14.5f, 10.38f, 13.38f, 11.5f, 12f, 11.5f)
                close()
            }
        }.build()
    }

    val Logout: ImageVector by lazy {
        ImageVector.Builder(
            name = "Logout",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFF1E293B))) {
                moveTo(17f, 7f)
                lineTo(15.59f, 8.41f)
                lineTo(17.17f, 10f)
                horizontalLineTo(9f)
                verticalLineTo(12f)
                horizontalLineTo(17.17f)
                lineTo(15.59f, 13.59f)
                lineTo(17f, 15f)
                lineTo(21f, 11f)
                lineTo(17f, 7f)
                close()
                moveTo(19f, 3f)
                horizontalLineTo(5f)
                curveTo(3.9f, 3f, 3f, 3.9f, 3f, 5f)
                verticalLineTo(19f)
                curveTo(3f, 20.1f, 3.9f, 21f, 5f, 21f)
                horizontalLineTo(19f)
                curveTo(20.1f, 21f, 21f, 20.1f, 21f, 19f)
                verticalLineTo(16f)
                horizontalLineTo(19f)
                verticalLineTo(19f)
                horizontalLineTo(5f)
                verticalLineTo(5f)
                horizontalLineTo(19f)
                verticalLineTo(8f)
                horizontalLineTo(21f)
                verticalLineTo(5f)
                curveTo(21f, 3.9f, 20.1f, 3f, 19f, 3f)
                close()
            }
        }.build()
    }

    val Work: ImageVector by lazy {
        ImageVector.Builder(
            name = "Work",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFF1E293B))) {
                moveTo(20f, 6f)
                horizontalLineTo(16f)
                verticalLineTo(4f)
                curveTo(16f, 2.9f, 15.1f, 2f, 14f, 2f)
                horizontalLineTo(10f)
                curveTo(8.9f, 2f, 8f, 2.9f, 8f, 4f)
                verticalLineTo(6f)
                horizontalLineTo(4f)
                curveTo(2.9f, 6f, 2f, 6.9f, 2f, 8f)
                verticalLineTo(19f)
                curveTo(2f, 20.1f, 2.9f, 21f, 4f, 21f)
                horizontalLineTo(20f)
                curveTo(21.1f, 21f, 22f, 20.1f, 22f, 19f)
                verticalLineTo(8f)
                curveTo(22f, 6.9f, 21.1f, 6f, 20f, 6f)
                close()
                moveTo(10f, 4f)
                horizontalLineTo(14f)
                verticalLineTo(6f)
                horizontalLineTo(10f)
                verticalLineTo(4f)
                close()
            }
        }.build()
    }

    val Shield: ImageVector by lazy {
        ImageVector.Builder(
            name = "Shield",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFF1E293B))) {
                moveTo(12f, 1f)
                lineTo(3f, 5f)
                verticalLineTo(11f)
                curveTo(3f, 16.55f, 6.84f, 21.74f, 12f, 23f)
                curveTo(17.16f, 21.74f, 21f, 16.55f, 21f, 11f)
                verticalLineTo(5f)
                lineTo(12f, 1f)
                close()
                moveTo(10f, 16.5f)
                lineTo(6f, 12.5f)
                lineTo(7.41f, 11.09f)
                lineTo(10f, 13.67f)
                lineTo(16.59f, 7.09f)
                lineTo(18f, 8.5f)
                lineTo(10f, 16.5f)
                close()
            }
        }.build()
    }

    val Visibility: ImageVector by lazy {
        ImageVector.Builder(
            name = "Visibility",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFF1E293B))) {
                moveTo(12f, 4.5f)
                curveTo(7f, 4.5f, 2.73f, 7.61f, 1f, 12f)
                curveTo(2.73f, 16.39f, 7f, 19.5f, 12f, 19.5f)
                curveTo(17f, 19.5f, 21.27f, 16.39f, 23f, 12f)
                curveTo(21.27f, 7.61f, 17f, 4.5f, 12f, 4.5f)
                close()
                moveTo(12f, 17f)
                curveTo(9.24f, 17f, 7f, 14.76f, 7f, 12f)
                curveTo(7f, 9.24f, 9.24f, 7f, 12f, 7f)
                curveTo(14.76f, 7f, 17f, 9.24f, 17f, 12f)
                curveTo(17f, 14.76f, 14.76f, 17f, 12f, 17f)
                close()
                moveTo(12f, 9f)
                curveTo(10.34f, 9f, 9f, 10.34f, 9f, 12f)
                curveTo(9f, 13.66f, 10.34f, 15f, 12f, 15f)
                curveTo(13.66f, 15f, 15f, 13.66f, 15f, 12f)
                curveTo(15f, 10.34f, 13.66f, 9f, 12f, 9f)
                close()
            }
        }.build()
    }

    val VisibilityOff: ImageVector by lazy {
        ImageVector.Builder(
            name = "VisibilityOff",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFF1E293B))) {
                moveTo(12f, 7f)
                curveTo(13.66f, 7f, 15f, 8.34f, 15f, 10f)
                curveTo(15f, 10.64f, 14.8f, 11.23f, 14.46f, 11.72f)
                lineTo(16.71f, 13.97f)
                curveTo(17.51f, 12.8f, 18f, 11.45f, 18f, 10f)
                curveTo(18f, 6.69f, 15.31f, 4f, 12f, 4f)
                curveTo(10.55f, 4f, 9.2f, 4.49f, 8.03f, 5.29f)
                lineTo(10.28f, 7.54f)
                curveTo(10.77f, 7.2f, 11.36f, 7f, 12f, 7f)
                close()
                moveTo(2f, 4.27f)
                lineTo(4.28f, 6.55f)
                curveTo(2.88f, 7.74f, 1.76f, 9.25f, 1f, 11f)
                curveTo(2.73f, 15.39f, 7f, 18.5f, 12f, 18.5f)
                curveTo(13.75f, 18.5f, 15.41f, 18.06f, 16.89f, 17.29f)
                lineTo(19.73f, 20.13f)
                lineTo(21f, 18.86f)
                lineTo(3.27f, 3f)
                lineTo(2f, 4.27f)
                close()
            }
        }.build()
    }

    val Search: ImageVector by lazy {
        ImageVector.Builder(
            name = "Search",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFF1E293B))) {
                moveTo(15.5f, 14f)
                horizontalLineTo(14.71f)
                lineTo(14.43f, 13.73f)
                curveTo(15.41f, 12.59f, 16f, 11.11f, 16f, 9.5f)
                curveTo(16f, 5.91f, 13.09f, 3f, 9.5f, 3f)
                curveTo(5.91f, 3f, 3f, 5.91f, 3f, 9.5f)
                curveTo(3f, 13.09f, 5.91f, 16f, 9.5f, 16f)
                curveTo(11.11f, 16f, 12.59f, 15.41f, 13.73f, 14.43f)
                lineTo(14f, 14.71f)
                verticalLineTo(15.5f)
                lineTo(19f, 20.49f)
                lineTo(20.49f, 19f)
                lineTo(15.5f, 14f)
                close()
                moveTo(9.5f, 14f)
                curveTo(7.01f, 14f, 5f, 11.99f, 5f, 9.5f)
                curveTo(5f, 7.01f, 7.01f, 5f, 9.5f, 5f)
                curveTo(11.99f, 5f, 14f, 7.01f, 14f, 9.5f)
                curveTo(14f, 11.99f, 11.99f, 14f, 9.5f, 14f)
                close()
            }
        }.build()
    }

    val Notifications: ImageVector by lazy {
        ImageVector.Builder(
            name = "Notifications",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFF1E293B))) {
                moveTo(12f, 22f)
                curveTo(13.1f, 22f, 14f, 21.1f, 14f, 20f)
                horizontalLineTo(10f)
                curveTo(10f, 21.1f, 10.9f, 22f, 12f, 22f)
                close()
                moveTo(18f, 16f)
                verticalLineTo(11f)
                curveTo(18f, 7.93f, 16.37f, 5.36f, 13.5f, 4.68f)
                verticalLineTo(4f)
                curveTo(13.5f, 3.17f, 12.83f, 2.5f, 12f, 2.5f)
                curveTo(11.17f, 2.5f, 10.5f, 3.17f, 10.5f, 4f)
                verticalLineTo(4.68f)
                curveTo(7.64f, 5.36f, 6f, 7.92f, 6f, 11f)
                verticalLineTo(16f)
                lineTo(4f, 18f)
                verticalLineTo(19f)
                horizontalLineTo(20f)
                verticalLineTo(18f)
                lineTo(18f, 16f)
                close()
            }
        }.build()
    }

    val Mic: ImageVector by lazy {
        ImageVector.Builder(
            name = "Mic",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFF1E293B))) {
                moveTo(12f, 14f)
                curveTo(13.66f, 14f, 15f, 12.66f, 15f, 11f)
                verticalLineTo(5f)
                curveTo(15f, 3.34f, 13.66f, 2f, 12f, 2f)
                curveTo(10.34f, 2f, 9f, 3.34f, 9f, 5f)
                verticalLineTo(11f)
                curveTo(9f, 12.66f, 10.34f, 14f, 12f, 14f)
                close()
                moveTo(17f, 11f)
                curveTo(17f, 13.76f, 14.76f, 16f, 12f, 16f)
                curveTo(9.24f, 16f, 7f, 13.76f, 7f, 11f)
                horizontalLineTo(5f)
                curveTo(5f, 14.53f, 7.72f, 17.4f, 11f, 17.93f)
                verticalLineTo(21f)
                horizontalLineTo(13f)
                verticalLineTo(17.93f)
                curveTo(16.28f, 17.4f, 19f, 14.53f, 19f, 11f)
                horizontalLineTo(17f)
                close()
            }
        }.build()
    }

    val FilterList: ImageVector by lazy {
        ImageVector.Builder(
            name = "FilterList",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFF1E293B))) {
                moveTo(10f, 18f)
                horizontalLineTo(14f)
                verticalLineTo(16f)
                horizontalLineTo(10f)
                verticalLineTo(18f)
                close()
                moveTo(3f, 6f)
                verticalLineTo(8f)
                horizontalLineTo(21f)
                verticalLineTo(6f)
                horizontalLineTo(3f)
                close()
                moveTo(6f, 13f)
                horizontalLineTo(18f)
                verticalLineTo(11f)
                horizontalLineTo(6f)
                verticalLineTo(13f)
                close()
            }
        }.build()
    }

    val Home: ImageVector by lazy {
        ImageVector.Builder(
            name = "Home",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFF1E293B))) {
                moveTo(10f, 20f)
                verticalLineTo(14f)
                horizontalLineTo(14f)
                verticalLineTo(20f)
                horizontalLineTo(19f)
                verticalLineTo(12f)
                horizontalLineTo(22f)
                lineTo(12f, 3f)
                lineTo(2f, 12f)
                horizontalLineTo(5f)
                verticalLineTo(20f)
                horizontalLineTo(10f)
                close()
            }
        }.build()
    }

    val Assignment: ImageVector by lazy {
        ImageVector.Builder(
            name = "Assignment",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFF1E293B))) {
                moveTo(19f, 3f)
                horizontalLineTo(14.82f)
                curveTo(14.4f, 1.84f, 13.3f, 1f, 12f, 1f)
                curveTo(10.7f, 1f, 9.6f, 1.84f, 9.18f, 3f)
                horizontalLineTo(5f)
                curveTo(3.9f, 3f, 3f, 3.9f, 3f, 5f)
                verticalLineTo(19f)
                curveTo(3f, 20.1f, 3.9f, 21f, 5f, 21f)
                horizontalLineTo(19f)
                curveTo(20.1f, 21f, 21f, 20.1f, 21f, 19f)
                verticalLineTo(5f)
                curveTo(21f, 3.9f, 20.1f, 3f, 19f, 3f)
                close()
                moveTo(12f, 3f)
                curveTo(12.55f, 3f, 13f, 3.45f, 13f, 4f)
                curveTo(13f, 4.55f, 12.55f, 5f, 12f, 5f)
                curveTo(11.45f, 5f, 11f, 4.55f, 11f, 4f)
                curveTo(11f, 3.45f, 11.45f, 3f, 12f, 3f)
                close()
                moveTo(14f, 17f)
                horizontalLineTo(7f)
                verticalLineTo(15f)
                horizontalLineTo(14f)
                verticalLineTo(17f)
                close()
                moveTo(17f, 13f)
                horizontalLineTo(7f)
                verticalLineTo(11f)
                horizontalLineTo(17f)
                verticalLineTo(13f)
                close()
                moveTo(17f, 9f)
                horizontalLineTo(7f)
                verticalLineTo(7f)
                horizontalLineTo(17f)
                verticalLineTo(9f)
                close()
            }
        }.build()
    }

    val Support: ImageVector by lazy {
        ImageVector.Builder(
            name = "Support",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFF1E293B))) {
                moveTo(12f, 1f)
                curveTo(5.93f, 1f, 1f, 5.93f, 1f, 12f)
                curveTo(1f, 18.07f, 5.93f, 23f, 12f, 23f)
                curveTo(18.07f, 23f, 23f, 18.07f, 23f, 12f)
                curveTo(23f, 5.93f, 18.07f, 1f, 12f, 1f)
                close()
                moveTo(12f, 21f)
                curveTo(7.03f, 21f, 3f, 16.97f, 3f, 12f)
                curveTo(3f, 7.03f, 7.03f, 3f, 12f, 3f)
                curveTo(16.97f, 3f, 21f, 7.03f, 21f, 12f)
                curveTo(21f, 16.97f, 16.97f, 21f, 12f, 21f)
                close()
                moveTo(12f, 6.5f)
                curveTo(9.51f, 6.5f, 7.5f, 8.51f, 7.5f, 11f)
                horizontalLineTo(9.5f)
                curveTo(9.5f, 9.62f, 10.62f, 8.5f, 12f, 8.5f)
                curveTo(13.38f, 8.5f, 14.5f, 9.62f, 14.5f, 11f)
                curveTo(14.5f, 13f, 11.5f, 12.75f, 11.5f, 16f)
                horizontalLineTo(13.5f)
                curveTo(13.5f, 13.75f, 16.5f, 13.5f, 16.5f, 11f)
                curveTo(16.5f, 8.51f, 14.49f, 6.5f, 12f, 6.5f)
                close()
                moveTo(11.5f, 17.5f)
                horizontalLineTo(13.5f)
                verticalLineTo(19.5f)
                horizontalLineTo(11.5f)
                close()
            }
        }.build()
    }

    val Emergency: ImageVector by lazy {
        ImageVector.Builder(
            name = "Emergency",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFFDC2626))) {
                moveTo(1f, 21f)
                horizontalLineTo(23f)
                lineTo(12f, 2f)
                lineTo(1f, 21f)
                close()
                moveTo(13f, 18f)
                horizontalLineTo(11f)
                verticalLineTo(16f)
                horizontalLineTo(13f)
                verticalLineTo(18f)
                close()
                moveTo(13f, 14f)
                horizontalLineTo(11f)
                verticalLineTo(10f)
                horizontalLineTo(13f)
                verticalLineTo(14f)
                close()
            }
        }.build()
    }

    val Add: ImageVector by lazy {
        ImageVector.Builder(
            name = "Add",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFF1E293B))) {
                moveTo(19f, 13f)
                horizontalLineTo(13f)
                verticalLineTo(19f)
                horizontalLineTo(11f)
                verticalLineTo(13f)
                horizontalLineTo(5f)
                verticalLineTo(11f)
                horizontalLineTo(11f)
                verticalLineTo(5f)
                horizontalLineTo(13f)
                verticalLineTo(11f)
                horizontalLineTo(19f)
                verticalLineTo(13f)
                close()
            }
        }.build()
    }

    val Close: ImageVector by lazy {
        ImageVector.Builder(
            name = "Close",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFF1E293B))) {
                moveTo(19f, 6.41f)
                lineTo(17.59f, 5f)
                lineTo(12f, 10.59f)
                lineTo(6.41f, 5f)
                lineTo(5f, 6.41f)
                lineTo(10.59f, 12f)
                lineTo(5f, 17.59f)
                lineTo(6.41f, 19f)
                lineTo(12f, 13.41f)
                lineTo(17.59f, 19f)
                lineTo(19f, 17.59f)
                lineTo(13.41f, 12f)
                close()
            }
        }.build()
    }

    val Refresh: ImageVector by lazy {
        ImageVector.Builder(
            name = "Refresh",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFF1E293B))) {
                moveTo(17.65f, 6.35f)
                curveTo(16.2f, 4.9f, 14.21f, 4f, 12f, 4f)
                curveTo(7.58f, 4f, 4.01f, 7.58f, 4.01f, 12f)
                curveTo(4.01f, 16.42f, 7.58f, 20f, 12f, 20f)
                curveTo(15.73f, 20f, 18.84f, 17.45f, 19.73f, 14f)
                horizontalLineTo(17.65f)
                curveTo(16.83f, 16.33f, 14.61f, 18f, 12f, 18f)
                curveTo(8.69f, 18f, 6f, 15.31f, 6f, 12f)
                curveTo(6f, 8.69f, 8.69f, 6f, 12f, 6f)
                curveTo(13.66f, 6f, 15.14f, 6.69f, 16.22f, 7.78f)
                lineTo(13f, 11f)
                horizontalLineTo(20f)
                verticalLineTo(4f)
                lineTo(17.65f, 6.35f)
                close()
            }
        }.build()
    }

    val Calendar: ImageVector by lazy {
        ImageVector.Builder(
            name = "Calendar",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFF1E293B))) {
                moveTo(19f, 4f)
                horizontalLineTo(18f)
                verticalLineTo(2f)
                horizontalLineTo(16f)
                verticalLineTo(4f)
                horizontalLineTo(8f)
                verticalLineTo(2f)
                horizontalLineTo(6f)
                verticalLineTo(4f)
                horizontalLineTo(5f)
                curveTo(3.89f, 4f, 3.01f, 4.9f, 3.01f, 6f)
                lineTo(3f, 20f)
                curveTo(3f, 21.1f, 3.89f, 22f, 5f, 22f)
                horizontalLineTo(19f)
                curveTo(20.1f, 22f, 21f, 21.1f, 21f, 20f)
                verticalLineTo(6f)
                curveTo(21f, 4.9f, 20.1f, 4f, 19f, 4f)
                close()
                moveTo(19f, 20f)
                horizontalLineTo(5f)
                verticalLineTo(10f)
                horizontalLineTo(19f)
                verticalLineTo(20f)
                close()
                moveTo(19f, 8f)
                horizontalLineTo(5f)
                verticalLineTo(6f)
                horizontalLineTo(19f)
                verticalLineTo(8f)
                close()
            }
        }.build()
    }

    val Group: ImageVector by lazy {
        ImageVector.Builder(
            name = "Group",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFF1E293B))) {
                moveTo(16f, 11f)
                curveTo(17.66f, 11f, 18.99f, 9.66f, 18.99f, 8f)
                curveTo(18.99f, 6.34f, 17.66f, 5f, 16f, 5f)
                curveTo(14.34f, 5f, 13f, 6.34f, 13f, 8f)
                curveTo(13f, 9.66f, 14.34f, 11f, 16f, 11f)
                close()
                moveTo(8f, 11f)
                curveTo(9.66f, 11f, 10.99f, 9.66f, 10.99f, 8f)
                curveTo(10.99f, 6.34f, 9.66f, 5f, 8f, 5f)
                curveTo(6.34f, 5f, 5f, 6.34f, 5f, 8f)
                curveTo(5f, 9.66f, 6.34f, 11f, 8f, 11f)
                close()
                moveTo(8f, 13f)
                curveTo(5.67f, 13f, 1f, 14.17f, 1f, 16.5f)
                verticalLineTo(19f)
                horizontalLineTo(15f)
                verticalLineTo(16.5f)
                curveTo(15f, 14.17f, 10.33f, 13f, 8f, 13f)
                close()
                moveTo(16f, 13f)
                curveTo(15.71f, 13f, 15.38f, 13.02f, 15.03f, 13.05f)
                curveTo(16.19f, 13.89f, 17f, 15.02f, 17f, 16.5f)
                verticalLineTo(19f)
                horizontalLineTo(23f)
                verticalLineTo(16.5f)
                curveTo(23f, 14.17f, 18.33f, 13f, 16f, 13f)
                close()
            }
        }.build()
    }

    val CurrencyRupee: ImageVector by lazy {
        ImageVector.Builder(
            name = "CurrencyRupee",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFF1E293B))) {
                moveTo(13.66f, 7f)
                curveTo(13.1f, 5.82f, 11.9f, 5f, 10.5f, 5f)
                horizontalLineTo(6f)
                verticalLineTo(3f)
                horizontalLineTo(18f)
                verticalLineTo(5f)
                horizontalLineTo(14.74f)
                curveTo(15.22f, 5.58f, 15.54f, 6.26f, 15.65f, 7f)
                horizontalLineTo(18f)
                verticalLineTo(9f)
                horizontalLineTo(15.65f)
                curveTo(15.26f, 11.47f, 13.12f, 13.34f, 10.5f, 13.48f)
                lineTo(16f, 21f)
                horizontalLineTo(13.24f)
                lineTo(7.94f, 13.5f)
                verticalLineTo(11f)
                horizontalLineTo(10.5f)
                curveTo(11.96f, 11f, 13.17f, 10.15f, 13.66f, 9f)
                horizontalLineTo(6f)
                verticalLineTo(7f)
                horizontalLineTo(13.66f)
                close()
            }
        }.build()
    }

    val Call: ImageVector by lazy {
        ImageVector.Builder(
            name = "Call",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFF1E293B))) {
                moveTo(20.01f, 15.38f)
                curveTo(18.78f, 15.38f, 17.59f, 15.18f, 16.48f, 14.82f)
                curveTo(16.13f, 14.7f, 15.74f, 14.79f, 15.47f, 15.06f)
                lineTo(13.27f, 17.26f)
                curveTo(10.44f, 15.82f, 8.13f, 13.51f, 6.69f, 10.68f)
                lineTo(8.89f, 8.48f)
                curveTo(9.17f, 8.2f, 9.25f, 7.82f, 9.14f, 7.47f)
                curveTo(8.77f, 6.36f, 8.57f, 5.17f, 8.57f, 3.94f)
                curveTo(8.57f, 3.42f, 8.15f, 3f, 7.63f, 3f)
                horizontalLineTo(4.06f)
                curveTo(3.55f, 3f, 3f, 3.42f, 3f, 3.94f)
                curveTo(3f, 13.36f, 10.64f, 21f, 20.06f, 21f)
                curveTo(20.58f, 21f, 21f, 20.58f, 21f, 20.06f)
                verticalLineTo(16.5f)
                curveTo(21f, 15.98f, 20.53f, 15.38f, 20.01f, 15.38f)
                close()
            }
        }.build()
    }

    val Menu: ImageVector by lazy {
        ImageVector.Builder(
            name = "Menu",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFF1E293B))) {
                moveTo(3f, 18f)
                horizontalLineTo(21f)
                verticalLineTo(16f)
                horizontalLineTo(3f)
                verticalLineTo(18f)
                close()
                moveTo(3f, 13f)
                horizontalLineTo(21f)
                verticalLineTo(11f)
                horizontalLineTo(3f)
                verticalLineTo(13f)
                close()
                moveTo(3f, 6f)
                verticalLineTo(8f)
                horizontalLineTo(21f)
                verticalLineTo(6f)
                horizontalLineTo(3f)
                close()
            }
        }.build()
    }

    val VolumeUp: ImageVector by lazy {
        ImageVector.Builder(
            name = "VolumeUp",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFF1E293B))) {
                moveTo(3f, 9f)
                verticalLineTo(15f)
                horizontalLineTo(7f)
                lineTo(12f, 20f)
                verticalLineTo(4f)
                lineTo(7f, 9f)
                horizontalLineTo(3f)
                close()
                moveTo(16.5f, 12f)
                curveTo(16.5f, 10.23f, 15.48f, 8.71f, 14f, 7.97f)
                verticalLineTo(16.02f)
                curveTo(15.48f, 15.29f, 16.5f, 13.77f, 16.5f, 12f)
                close()
                moveTo(14f, 3.23f)
                verticalLineTo(5.29f)
                curveTo(16.89f, 6.15f, 19f, 8.83f, 19f, 12f)
                curveTo(19f, 15.17f, 16.89f, 17.85f, 14f, 18.71f)
                verticalLineTo(20.77f)
                curveTo(18.01f, 19.86f, 21f, 16.28f, 21f, 12f)
                curveTo(21f, 7.72f, 18.01f, 4.14f, 14f, 3.23f)
                close()
            }
        }.build()
    }

    val Send: ImageVector by lazy {
        ImageVector.Builder(
            name = "Send",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFF1E293B))) {
                moveTo(2.01f, 21f)
                lineTo(23f, 12f)
                lineTo(2.01f, 3f)
                lineTo(2f, 10f)
                lineTo(17f, 12f)
                lineTo(2f, 14f)
                close()
            }
        }.build()
    }

    val Scale: ImageVector by lazy {
        ImageVector.Builder(
            name = "Scale",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFF1E293B))) {
                moveTo(12f, 2f)
                curveTo(11.45f, 2f, 11f, 2.45f, 11f, 3f)
                verticalLineTo(5.08f)
                lineTo(4.41f, 7.27f)
                curveTo(3.87f, 7.45f, 3.66f, 8.11f, 3.99f, 8.57f)
                lineTo(6.5f, 12f)
                horizontalLineTo(2f)
                curveTo(2f, 14.21f, 3.79f, 16f, 6f, 16f)
                curveTo(8.21f, 16f, 10f, 14.21f, 10f, 12f)
                horizontalLineTo(7.85f)
                lineTo(5.8f, 9.14f)
                lineTo(11f, 7.41f)
                verticalLineTo(20f)
                horizontalLineTo(8f)
                verticalLineTo(22f)
                horizontalLineTo(16f)
                verticalLineTo(20f)
                horizontalLineTo(13f)
                verticalLineTo(7.41f)
                lineTo(18.2f, 9.14f)
                lineTo(16.15f, 12f)
                horizontalLineTo(14f)
                curveTo(14f, 14.21f, 15.79f, 16f, 18f, 16f)
                curveTo(20.21f, 16f, 22f, 14.21f, 22f, 12f)
                horizontalLineTo(17.5f)
                lineTo(20.01f, 8.57f)
                curveTo(20.34f, 8.11f, 20.13f, 7.45f, 19.59f, 7.27f)
                lineTo(13f, 5.08f)
                verticalLineTo(3f)
                curveTo(13f, 2.45f, 12.55f, 2f, 12f, 2f)
                close()
            }
        }.build()
    }

    val Description: ImageVector by lazy {
        ImageVector.Builder(
            name = "Description",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFF1E293B))) {
                moveTo(14f, 2f)
                horizontalLineTo(6f)
                curveTo(4.9f, 2f, 4f, 2.9f, 4f, 4f)
                verticalLineTo(20f)
                curveTo(4f, 21.1f, 4.9f, 22f, 6f, 22f)
                horizontalLineTo(18f)
                curveTo(19.1f, 22f, 20f, 21.1f, 20f, 20f)
                verticalLineTo(8f)
                lineTo(14f, 2f)
                close()
                moveTo(16f, 18f)
                horizontalLineTo(8f)
                verticalLineTo(16f)
                horizontalLineTo(16f)
                verticalLineTo(18f)
                close()
                moveTo(16f, 14f)
                horizontalLineTo(8f)
                verticalLineTo(12f)
                horizontalLineTo(16f)
                verticalLineTo(14f)
                close()
                moveTo(13f, 9f)
                verticalLineTo(3.5f)
                lineTo(18.5f, 9f)
                horizontalLineTo(13f)
                close()
            }
        }.build()
    }

    val Schedule: ImageVector by lazy {
        ImageVector.Builder(
            name = "Schedule",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFF1E293B))) {
                moveTo(11.99f, 2f)
                curveTo(6.47f, 2f, 2f, 6.48f, 2f, 12f)
                curveTo(2f, 17.52f, 6.47f, 22f, 11.99f, 22f)
                curveTo(17.52f, 22f, 22f, 17.52f, 22f, 12f)
                curveTo(22f, 6.48f, 17.52f, 2f, 11.99f, 2f)
                close()
                moveTo(12f, 20f)
                curveTo(7.58f, 20f, 4f, 16.42f, 4f, 12f)
                curveTo(4f, 7.58f, 7.58f, 4f, 12f, 4f)
                curveTo(16.42f, 4f, 20f, 7.58f, 20f, 12f)
                curveTo(20f, 16.42f, 16.42f, 20f, 12f, 20f)
                close()
                moveTo(12.5f, 7f)
                horizontalLineTo(11f)
                verticalLineTo(13f)
                lineTo(16.25f, 16.15f)
                lineTo(17f, 14.92f)
                lineTo(12.5f, 12.25f)
                verticalLineTo(7f)
                close()
            }
        }.build()
    }

    val MoreHoriz: ImageVector by lazy {
        ImageVector.Builder(
            name = "MoreHoriz",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFF1E293B))) {
                moveTo(6f, 10f)
                curveTo(4.9f, 10f, 4f, 10.9f, 4f, 12f)
                curveTo(4f, 13.1f, 4.9f, 14f, 6f, 14f)
                curveTo(7.1f, 14f, 8f, 13.1f, 8f, 12f)
                curveTo(8f, 10.9f, 7.1f, 10f, 6f, 10f)
                close()
                moveTo(12f, 10f)
                curveTo(10.9f, 10f, 10f, 10.9f, 10f, 12f)
                curveTo(10f, 13.1f, 10.9f, 14f, 12f, 14f)
                curveTo(13.1f, 14f, 14f, 13.1f, 14f, 12f)
                curveTo(14f, 10.9f, 13.1f, 10f, 12f, 10f)
                close()
                moveTo(18f, 10f)
                curveTo(16.9f, 10f, 16f, 10.9f, 16f, 12f)
                curveTo(16f, 13.1f, 16.9f, 14f, 18f, 14f)
                curveTo(19.1f, 14f, 20f, 13.1f, 20f, 12f)
                curveTo(20f, 10.9f, 19.1f, 10f, 18f, 10f)
                close()
            }
        }.build()
    }

    val Edit: ImageVector by lazy {
        ImageVector.Builder(
            name = "Edit",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFF1E293B))) {
                moveTo(3f, 17.25f)
                verticalLineTo(21f)
                horizontalLineTo(6.75f)
                lineTo(17.81f, 9.94f)
                lineTo(14.06f, 6.19f)
                lineTo(3f, 17.25f)
                close()
                moveTo(20.71f, 7.04f)
                curveTo(21.1f, 6.65f, 21.1f, 6.02f, 20.71f, 5.63f)
                lineTo(18.37f, 3.29f)
                curveTo(17.98f, 2.9f, 17.35f, 2.9f, 16.96f, 3.29f)
                lineTo(15.13f, 5.12f)
                lineTo(18.88f, 8.87f)
                lineTo(20.71f, 7.04f)
                close()
            }
        }.build()
    }

    val Photo: ImageVector by lazy {
        ImageVector.Builder(
            name = "Photo",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFF1E293B))) {
                moveTo(21f, 19f)
                verticalLineTo(5f)
                curveTo(21f, 3.9f, 20.1f, 3f, 19f, 3f)
                horizontalLineTo(5f)
                curveTo(3.9f, 3f, 3f, 3.9f, 3f, 5f)
                verticalLineTo(19f)
                curveTo(3f, 20.1f, 3.9f, 21f, 5f, 21f)
                horizontalLineTo(19f)
                curveTo(20.1f, 21f, 21f, 20.1f, 21f, 19f)
                close()
                moveTo(8.5f, 13.5f)
                lineTo(11f, 16.51f)
                lineTo(14.5f, 12f)
                lineTo(19f, 18f)
                horizontalLineTo(5f)
                lineTo(8.5f, 13.5f)
                close()
            }
        }.build()
    }

    val SmartToy: ImageVector by lazy {
        ImageVector.Builder(
            name = "SmartToy",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFF1E293B))) {
                moveTo(20f, 9f)
                verticalLineTo(7f)
                curveTo(20f, 5.9f, 19.1f, 5f, 18f, 5f)
                horizontalLineTo(13f)
                verticalLineTo(3f)
                curveTo(13f, 2.45f, 12.55f, 2f, 12f, 2f)
                curveTo(11.45f, 2f, 11f, 2.45f, 11f, 3f)
                verticalLineTo(5f)
                horizontalLineTo(6f)
                curveTo(4.9f, 5f, 4f, 5.9f, 4f, 7f)
                verticalLineTo(9f)
                curveTo(2.9f, 9f, 2f, 9.9f, 2f, 11f)
                verticalLineTo(15f)
                curveTo(2f, 16.1f, 2.9f, 17f, 4f, 17f)
                verticalLineTo(19f)
                curveTo(4f, 20.1f, 4.9f, 21f, 6f, 21f)
                horizontalLineTo(18f)
                curveTo(19.1f, 21f, 20f, 20.1f, 20f, 19f)
                verticalLineTo(17f)
                curveTo(21.1f, 17f, 22f, 16.1f, 22f, 15f)
                verticalLineTo(11f)
                curveTo(22f, 9.9f, 21.1f, 9f, 20f, 9f)
                close()
                moveTo(7.5f, 11.5f)
                curveTo(7.5f, 10.67f, 8.17f, 10f, 9f, 10f)
                curveTo(9.83f, 10f, 10.5f, 10.67f, 10.5f, 11.5f)
                curveTo(10.5f, 12.33f, 9.83f, 13f, 9f, 13f)
                curveTo(8.17f, 13f, 7.5f, 12.33f, 7.5f, 11.5f)
                close()
                moveTo(15f, 13f)
                curveTo(14.17f, 13f, 13.5f, 12.33f, 13.5f, 11.5f)
                curveTo(13.5f, 10.67f, 14.17f, 10f, 15f, 10f)
                curveTo(15.83f, 10f, 16.5f, 10.67f, 16.5f, 11.5f)
                curveTo(16.5f, 12.33f, 15.83f, 13f, 15f, 13f)
                close()
                moveTo(16f, 17f)
                horizontalLineTo(8f)
                verticalLineTo(15.5f)
                horizontalLineTo(16f)
                verticalLineTo(17f)
                close()
            }
        }.build()
    }

    val ShieldAlert: ImageVector by lazy {
        ImageVector.Builder(
            name = "ShieldAlert",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFF1E293B))) {
                moveTo(12f, 2f)
                lineTo(4f, 5f)
                verticalLineTo(11.09f)
                curveTo(4f, 16.14f, 7.41f, 20.85f, 12f, 22f)
                curveTo(16.59f, 20.85f, 20f, 16.14f, 20f, 11.09f)
                verticalLineTo(5f)
                lineTo(12f, 2f)
                close()
                moveTo(11f, 7f)
                horizontalLineTo(13f)
                verticalLineTo(13f)
                horizontalLineTo(11f)
                verticalLineTo(7f)
                close()
                moveTo(11f, 15f)
                horizontalLineTo(13f)
                verticalLineTo(17f)
                horizontalLineTo(11f)
                verticalLineTo(15f)
                close()
            }
        }.build()
    }

    val Tune: ImageVector by lazy {
        ImageVector.Builder(
            name = "Tune",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFF1E293B))) {
                moveTo(3f, 17f)
                verticalLineTo(19f)
                horizontalLineTo(9f)
                verticalLineTo(17f)
                horizontalLineTo(3f)
                close()
                moveTo(3f, 5f)
                verticalLineTo(7f)
                horizontalLineTo(13f)
                verticalLineTo(5f)
                horizontalLineTo(3f)
                close()
                moveTo(13f, 21f)
                verticalLineTo(19f)
                horizontalLineTo(21f)
                verticalLineTo(17f)
                horizontalLineTo(13f)
                verticalLineTo(15f)
                horizontalLineTo(11f)
                verticalLineTo(21f)
                horizontalLineTo(13f)
                close()
                moveTo(7f, 9f)
                verticalLineTo(11f)
                horizontalLineTo(3f)
                verticalLineTo(13f)
                horizontalLineTo(7f)
                verticalLineTo(15f)
                horizontalLineTo(9f)
                verticalLineTo(9f)
                horizontalLineTo(7f)
                close()
                moveTo(21f, 13f)
                verticalLineTo(11f)
                horizontalLineTo(11f)
                verticalLineTo(13f)
                horizontalLineTo(21f)
                close()
                moveTo(17f, 9f)
                horizontalLineTo(19f)
                verticalLineTo(7f)
                horizontalLineTo(21f)
                verticalLineTo(5f)
                horizontalLineTo(19f)
                verticalLineTo(3f)
                horizontalLineTo(17f)
                verticalLineTo(9f)
                close()
            }
        }.build()
    }

    val BookmarkBorder: ImageVector by lazy {
        ImageVector.Builder(
            name = "BookmarkBorder",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFF1E293B))) {
                moveTo(17f, 3f)
                horizontalLineTo(7f)
                curveTo(5.9f, 3f, 5f, 3.9f, 5f, 5f)
                verticalLineTo(21f)
                lineTo(12f, 18f)
                lineTo(19f, 21f)
                verticalLineTo(5f)
                curveTo(19f, 3.9f, 18.1f, 3f, 17f, 3f)
                close()
                moveTo(17f, 18f)
                lineTo(12f, 15.82f)
                lineTo(7f, 18f)
                verticalLineTo(5f)
                horizontalLineTo(17f)
                verticalLineTo(18f)
                close()
            }
        }.build()
    }

    val Bookmark: ImageVector by lazy {
        ImageVector.Builder(
            name = "Bookmark",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFF1E293B))) {
                moveTo(17f, 3f)
                horizontalLineTo(7f)
                curveTo(5.9f, 3f, 5f, 3.9f, 5f, 5f)
                verticalLineTo(21f)
                lineTo(12f, 18f)
                lineTo(19f, 21f)
                verticalLineTo(5f)
                curveTo(19f, 3.9f, 18.1f, 3f, 17f, 3f)
                close()
            }
        }.build()
    }

    val Settings: ImageVector by lazy {
        ImageVector.Builder(
            name = "Settings",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFF1E293B))) {
                moveTo(19.14f, 12.94f)
                curveTo(19.18f, 12.63f, 19.2f, 12.32f, 19.2f, 12f)
                curveTo(19.2f, 11.68f, 19.18f, 11.37f, 19.14f, 11.06f)
                lineTo(21.4f, 9.3f)
                curveTo(21.6f, 9.14f, 21.66f, 8.85f, 21.53f, 8.62f)
                lineTo(19.38f, 4.9f)
                curveTo(19.25f, 4.67f, 18.96f, 4.58f, 18.73f, 4.68f)
                lineTo(16.07f, 5.75f)
                curveTo(15.52f, 5.32f, 14.91f, 4.98f, 14.25f, 4.74f)
                lineTo(13.85f, 1.91f)
                curveTo(13.82f, 1.65f, 13.6f, 1.45f, 13.33f, 1.45f)
                horizontalLineTo(9.02f)
                curveTo(8.75f, 1.45f, 8.53f, 1.65f, 8.5f, 1.91f)
                lineTo(8.1f, 4.74f)
                curveTo(7.44f, 4.98f, 6.83f, 5.32f, 6.28f, 5.75f)
                lineTo(3.62f, 4.68f)
                curveTo(3.39f, 4.58f, 3.1f, 4.67f, 2.97f, 4.9f)
                lineTo(0.82f, 8.62f)
                curveTo(0.69f, 8.85f, 0.75f, 9.14f, 0.95f, 9.3f)
                lineTo(3.21f, 11.06f)
                curveTo(3.17f, 11.37f, 3.15f, 11.68f, 3.15f, 12f)
                curveTo(3.15f, 12.32f, 3.17f, 12.63f, 3.21f, 12.94f)
                lineTo(0.95f, 14.7f)
                curveTo(0.75f, 14.86f, 0.69f, 15.15f, 0.82f, 15.38f)
                lineTo(2.97f, 19.1f)
                curveTo(3.1f, 19.33f, 3.39f, 19.42f, 3.62f, 19.32f)
                lineTo(6.28f, 18.25f)
                curveTo(6.83f, 18.68f, 7.44f, 19.02f, 8.1f, 19.26f)
                lineTo(8.5f, 22.09f)
                curveTo(8.53f, 22.35f, 8.75f, 22.55f, 9.02f, 22.55f)
                horizontalLineTo(13.33f)
                curveTo(13.6f, 22.55f, 13.82f, 22.35f, 13.85f, 22.09f)
                lineTo(14.25f, 19.26f)
                curveTo(14.91f, 19.02f, 15.52f, 18.68f, 16.07f, 18.25f)
                lineTo(18.73f, 19.32f)
                curveTo(18.96f, 19.42f, 19.25f, 19.33f, 19.38f, 19.1f)
                lineTo(21.53f, 15.38f)
                curveTo(21.66f, 15.15f, 21.6f, 14.86f, 21.4f, 14.7f)
                lineTo(19.14f, 12.94f)
                close()
                moveTo(11.18f, 15.5f)
                curveTo(9.25f, 15.5f, 7.68f, 13.93f, 7.68f, 12f)
                curveTo(7.68f, 10.07f, 9.25f, 8.5f, 11.18f, 8.5f)
                curveTo(13.11f, 8.5f, 14.68f, 10.07f, 14.68f, 12f)
                curveTo(14.68f, 13.93f, 13.11f, 15.5f, 11.18f, 15.5f)
                close()
            }
        }.build()
    }

    val CreditCard: ImageVector by lazy {
        ImageVector.Builder(
            name = "CreditCard",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFF1E293B))) {
                moveTo(20f, 4f)
                horizontalLineTo(4f)
                curveTo(2.89f, 4f, 2.01f, 4.89f, 2.01f, 6f)
                lineTo(2f, 18f)
                curveTo(2f, 19.1f, 2.89f, 20f, 4f, 20f)
                horizontalLineTo(20f)
                curveTo(21.1f, 20f, 22f, 19.1f, 22f, 18f)
                verticalLineTo(6f)
                curveTo(22f, 4.9f, 21.1f, 4f, 20f, 4f)
                close()
                moveTo(20f, 18f)
                horizontalLineTo(4f)
                verticalLineTo(12f)
                horizontalLineTo(20f)
                verticalLineTo(18f)
                close()
                moveTo(20f, 8f)
                horizontalLineTo(4f)
                verticalLineTo(6f)
                horizontalLineTo(20f)
                verticalLineTo(8f)
                close()
            }
        }.build()
    }

    val Badge: ImageVector by lazy {
        ImageVector.Builder(
            name = "Badge",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFF1E293B))) {
                moveTo(20f, 7f)
                horizontalLineTo(16f)
                verticalLineTo(4f)
                curveTo(16f, 2.9f, 15.1f, 2f, 14f, 2f)
                horizontalLineTo(10f)
                curveTo(8.9f, 2f, 8f, 2.9f, 8f, 4f)
                verticalLineTo(7f)
                horizontalLineTo(4f)
                curveTo(2.9f, 7f, 2f, 7.9f, 2f, 9f)
                verticalLineTo(20f)
                curveTo(2f, 21.1f, 2.9f, 22f, 4f, 22f)
                horizontalLineTo(20f)
                curveTo(21.1f, 22f, 22f, 21.1f, 22f, 20f)
                verticalLineTo(9f)
                curveTo(22f, 7.9f, 21.1f, 7f, 20f, 7f)
                close()
                moveTo(10f, 4f)
                horizontalLineTo(14f)
                verticalLineTo(7f)
                horizontalLineTo(10f)
                verticalLineTo(4f)
                close()
                moveTo(12f, 12f)
                curveTo(13.1f, 12f, 14f, 12.9f, 14f, 14f)
                curveTo(14f, 15.1f, 13.1f, 16f, 12f, 16f)
                curveTo(10.9f, 16f, 10f, 15.1f, 10f, 14f)
                curveTo(10f, 12.9f, 10.9f, 12f, 12f, 12f)
                close()
                moveTo(16f, 19f)
                horizontalLineTo(8f)
                verticalLineTo(18.25f)
                curveTo(8f, 16.92f, 10.67f, 16.25f, 12f, 16.25f)
                curveTo(13.33f, 16.25f, 16f, 16.92f, 16f, 18.25f)
                verticalLineTo(19f)
                close()
            }
        }.build()
    }

    val SwapHoriz: ImageVector by lazy {
        ImageVector.Builder(
            name = "SwapHoriz",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFF1E293B))) {
                moveTo(6.99f, 11f)
                lineTo(3f, 15f)
                lineTo(6.99f, 19f)
                verticalLineTo(16f)
                horizontalLineTo(14f)
                verticalLineTo(14f)
                horizontalLineTo(6.99f)
                verticalLineTo(11f)
                close()
                moveTo(21f, 9f)
                lineTo(17.01f, 5f)
                verticalLineTo(8f)
                horizontalLineTo(10f)
                verticalLineTo(10f)
                horizontalLineTo(17.01f)
                verticalLineTo(13f)
                lineTo(21f, 9f)
                close()
            }
        }.build()
    }

    val HelpOutline: ImageVector by lazy {
        ImageVector.Builder(
            name = "HelpOutline",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFF1E293B))) {
                moveTo(11f, 18f)
                horizontalLineTo(13f)
                verticalLineTo(16f)
                horizontalLineTo(11f)
                verticalLineTo(18f)
                close()
                moveTo(12f, 2f)
                curveTo(6.48f, 2f, 2f, 6.48f, 2f, 12f)
                curveTo(2f, 17.52f, 6.48f, 22f, 12f, 22f)
                curveTo(17.52f, 22f, 22f, 17.52f, 22f, 12f)
                curveTo(22f, 6.48f, 17.52f, 2f, 12f, 2f)
                close()
                moveTo(12f, 20f)
                curveTo(7.59f, 20f, 4f, 16.41f, 4f, 12f)
                curveTo(4f, 7.59f, 7.59f, 4f, 12f, 4f)
                curveTo(16.41f, 4f, 20f, 7.59f, 20f, 12f)
                curveTo(20f, 16.41f, 16.41f, 20f, 12f, 20f)
                close()
                moveTo(12f, 6f)
                curveTo(9.79f, 6f, 8f, 7.79f, 8f, 10f)
                horizontalLineTo(10f)
                curveTo(10f, 8.9f, 10.9f, 8f, 12f, 8f)
                curveTo(13.1f, 8f, 14f, 8.9f, 14f, 10f)
                curveTo(14f, 12f, 11f, 11.75f, 11f, 15f)
                horizontalLineTo(13f)
                curveTo(13f, 12.75f, 16f, 12.5f, 16f, 10f)
                curveTo(16f, 7.79f, 14.21f, 6f, 12f, 6f)
                close()
            }
        }.build()
    }

    val Camera: ImageVector by lazy {
        ImageVector.Builder(
            name = "Camera",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFF1E293B))) {
                moveTo(9f, 2f)
                lineTo(7.17f, 4f)
                horizontalLineTo(4f)
                curveTo(2.9f, 4f, 2f, 4.9f, 2f, 6f)
                verticalLineTo(18f)
                curveTo(2f, 19.1f, 2.9f, 20f, 4f, 20f)
                horizontalLineTo(20f)
                curveTo(21.1f, 20f, 22f, 19.1f, 22f, 18f)
                verticalLineTo(6f)
                curveTo(22f, 4.9f, 21.1f, 4f, 20f, 4f)
                horizontalLineTo(16.83f)
                lineTo(15f, 2f)
                horizontalLineTo(9f)
                close()
                moveTo(12f, 17f)
                curveTo(9.79f, 17f, 8f, 15.21f, 8f, 13f)
                curveTo(8f, 10.79f, 9.79f, 9f, 12f, 9f)
                curveTo(14.21f, 9f, 16f, 10.79f, 16f, 13f)
                curveTo(16f, 15.21f, 14.21f, 17f, 12f, 17f)
                close()
                moveTo(12f, 11f)
                curveTo(10.9f, 11f, 10f, 11.9f, 10f, 13f)
                curveTo(10f, 14.1f, 10.9f, 15f, 12f, 15f)
                curveTo(13.1f, 15f, 14f, 14.1f, 14f, 13f)
                curveTo(14f, 11.9f, 13.1f, 11f, 12f, 11f)
                close()
            }
        }.build()
    }
}


