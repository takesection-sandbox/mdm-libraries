package com.pigumer.mdm.security.darwin.enrollment

import spray.json.DefaultJsonProtocol

case class Device(serialNumber: String,
                  model: String,
                  description: String,
                  color: String,
                  assetTag: String,
                  profileStatus: String,
                  profileUuid: String,
                  profileAssignTime: String,
                  profilePushTime: String,
                  deviceAssignedDate: String,
                  deviceAssignedBy: String,
                  os: String,
                  device_family: String)

object DeviceJsonProtocol extends DefaultJsonProtocol {
  implicit val deviceJsonFormat = jsonFormat(
    Device,
    "serial_number",
    "model",
    "description",
    "color",
    "asset_tag",
    "profile_status",
    "profile_uuid",
    "profile_assign_time",
    "profile_push_time",
    "device_assigned_date",
    "device_assigned_by",
    "os",
    "device_family")
}
