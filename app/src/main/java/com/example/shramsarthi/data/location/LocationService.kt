package com.example.shramsarthi.data.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.content.ContextCompat

/**
 * Geographic coordinates representation.
 */
data class GeoPoint(
    val latitude: Double,
    val longitude: Double,
    val label: String = ""
)

/**
 * Service managing real GPS/Network device location and accurate geodesic distance calculations.
 * Supports manual city coordinate fallback when location permission is declined by the user.
 */
class LocationService(private val context: Context) {

    private val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager

    /**
     * Well-known verified center coordinates for key Indian construction hubs and districts.
     * Used for genuine distance calculations when a user selects or specifies a city name.
     */
    val knownCityCenters: Map<String, GeoPoint> = mapOf(
        "greater noida" to GeoPoint(28.4744, 77.5040, "Greater Noida, UP"),
        "ग्रेटर नोएडा" to GeoPoint(28.4744, 77.5040, "Greater Noida, UP"),
        "noida" to GeoPoint(28.5355, 77.3910, "Noida, UP"),
        "नोएडा" to GeoPoint(28.5355, 77.3910, "Noida, UP"),
        "delhi" to GeoPoint(28.6139, 77.2090, "Delhi NCR"),
        "दिल्ली" to GeoPoint(28.6139, 77.2090, "Delhi NCR"),
        "gurgaon" to GeoPoint(28.4595, 77.0266, "Gurugram, Haryana"),
        "गुरुग्राम" to GeoPoint(28.4595, 77.0266, "Gurugram, Haryana"),
        "ghaziabad" to GeoPoint(28.6692, 77.4538, "Ghaziabad, UP"),
        "गाजियाबाद" to GeoPoint(28.6692, 77.4538, "Ghaziabad, UP"),
        "faridabad" to GeoPoint(28.4089, 77.3178, "Faridabad, Haryana"),
        "फरीदाबाद" to GeoPoint(28.4089, 77.3178, "Faridabad, Haryana"),
        "jaipur" to GeoPoint(26.9124, 75.7873, "Jaipur, Rajasthan"),
        "जयपुर" to GeoPoint(26.9124, 75.7873, "Jaipur, Rajasthan"),
        "lucknow" to GeoPoint(26.8467, 80.9462, "Lucknow, UP"),
        "लखनऊ" to GeoPoint(26.8467, 80.9462, "Lucknow, UP"),
        "agra" to GeoPoint(27.1767, 78.0081, "Agra, UP"),
        "आगरा" to GeoPoint(27.1767, 78.0081, "Agra, UP"),
        "kanpur" to GeoPoint(26.4499, 80.3319, "Kanpur, UP"),
        "कानपुर" to GeoPoint(26.4499, 80.3319, "Kanpur, UP"),
        "patna" to GeoPoint(25.5941, 85.1376, "Patna, Bihar"),
        "पटना" to GeoPoint(25.5941, 85.1376, "Patna, Bihar")
    )

    fun hasLocationPermission(): Boolean {
        val fine = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val coarse = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        return fine || coarse
    }

    @SuppressLint("MissingPermission")
    fun getDeviceLastKnownLocation(): GeoPoint? {
        if (!hasLocationPermission() || locationManager == null) return null

        val providers = listOf(
            LocationManager.GPS_PROVIDER,
            LocationManager.NETWORK_PROVIDER,
            LocationManager.PASSIVE_PROVIDER
        )

        var bestLocation: Location? = null
        for (provider in providers) {
            try {
                if (locationManager.isProviderEnabled(provider)) {
                    val loc = locationManager.getLastKnownLocation(provider)
                    if (loc != null) {
                        if (bestLocation == null || loc.accuracy < bestLocation.accuracy) {
                            bestLocation = loc
                        }
                    }
                }
            } catch (_: SecurityException) {
                // Handled via permission check
            } catch (_: Exception) {
                // Ignore provider error
            }
        }

        return bestLocation?.let {
            GeoPoint(it.latitude, it.longitude, "Device GPS (${it.accuracy.toInt()}m accuracy)")
        }
    }

    /**
     * Resolves location coordinates from either a GPS point, recognized city name, or location string.
     */
    fun resolveCoordinates(query: String): GeoPoint? {
        val q = query.trim().lowercase()
        for ((key, point) in knownCityCenters) {
            if (q.contains(key) || key.contains(q)) {
                return point
            }
        }
        return null
    }

    /**
     * Computes genuine geodesic distance between two points in kilometers.
     * Uses Android Location.distanceBetween algorithm (WGS84 ellipsoidal model).
     */
    fun calculateDistanceKm(fromLat: Double, fromLon: Double, toLat: Double, toLon: Double): Double {
        val results = FloatArray(1)
        Location.distanceBetween(fromLat, fromLon, toLat, toLon, results)
        return (results[0] / 1000.0)
    }

    /**
     * Checks if a target location string or point is within maxRadiusKm from the origin.
     */
    fun isWithinRadius(
        origin: GeoPoint,
        targetLocationString: String,
        maxRadiusKm: Double
    ): Boolean {
        val targetPoint = resolveCoordinates(targetLocationString)
        return if (targetPoint != null) {
            val dist = calculateDistanceKm(origin.latitude, origin.longitude, targetPoint.latitude, targetPoint.longitude)
            dist <= maxRadiusKm
        } else {
            // If coordinates cannot be derived, fallback to string containment check
            val normOrigin = origin.label.lowercase()
            val normTarget = targetLocationString.lowercase()
            normOrigin.contains(normTarget) || normTarget.contains(normOrigin)
        }
    }
}
