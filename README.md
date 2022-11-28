# Keycloak RFID Authentication Flow

This is a plugin for the Keycloak software which creates a new authentication
flow that requires an RFID (a set of numbers, set as an attribute to
a user) and a x509 Certificate that is bound to the client sending the
RFID number.

#### Example of a curl request to the direct grant endpoint:

```shell
$ curl -X POST \
    "https://{YOUR_HOST}/realms/{REALM}/protocol/openid-connect/token/" \
    --data-urlencode "client_id={CLIENT_ID}" \
    --data-urlencode "client_secret={CLIENT_SECRET}" \
    --data-urlencode "grant_type={GRANT_TYPE}" \
    --data-urlencode "scope={SCOPE}" \
    --data-urlencode "rfid=1234" \
    --cacert "{PATH_TO_CA}" \
    --cert "{PATH_TO_CLIENT_CERT}" \
    --key "{PATH_TO_CLIENT_PRIVATE_KEY}"
```

This request will print an openid token that can be used with keycloak

```json
{
    "access_token": "{ACCESS_TOKEN}",
    "expires_in": 300,
    "refresh_expires_in": 1799,
    "refresh_token": "{REFRESH_TOKEN}",
    "token_type": "Bearer",
    "id_token": "{ID_TOKEN}",
    "not-before-policy": 0,
    "session_state": "6ae33c46-f133-45db-abf0-f8a27fc0b013",
    "scope": "openid email profile"
}
```