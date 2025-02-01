# G1 Basis
Open, multipurpose infrastructure for writing Android applications that talk to the [Even Realities G1](https://www.evenrealities.com/g1) glasses.

## Core
The **core** module contains the source code to the core library. 
This library allows for interfacing directly with the glasses through a simple abstraction that uses modern Android and Kotlin features like coroutines and Flow. 
When using this library directly, only one application can connect to and interface with the glasses at one time. The basis core lirbary is used under the hood by the **service**.

*(more details coming soon)*

## Service
The **service** module implements a shared Android service that multiple applications can use to interface simultaneously with the glasses.  
Applications interface with the service using the AIDL-defined generated code and a simple wrapper that exposes the service using coroutines and flow the way the Core library does.
The service also handles requesting the necessary permissions at runtime, so calling applications do not have to.

### 1. Initialization

Initialize the client by calling

```kotlin
val client = G1ServiceClient(applicationContext)
val success = client.open()
```

This starts the service (if it is not already running) and connects to it.
Similarly, when you do not need to use the service anymore, call

```kotlin
client.close()
```

In the example application (a single-activity Compose app) open() is called in the activity's onCreate() and close() is called in the onDestroy().

### 2. Service State

The client exposes its state through

```kotlin
client.state
```

client.state is a StateFlow that is null if the service is initalizing, or type 

```kotlin
data class G1ServiceState(
  val status: Int, 
    // values can be
    //    G1ServiceState.READY - the service is initialized and ready to scan for glasses
    //    G1ServiceState.LOOKING - the service is scanning for glasses
    //    G1ServiceState.LOOKED - the service has looked for glasses and is ready to look again
    //    G1ServiceState.ERROR - the service encountered an error looking for glasses
  val glasses: Array<G1Glasses>
    // glasses that the service has found
)
```

```kotlin
data class G1Glasses(
  val id: String,
    // unique id for glasses, treat as opaque (constructed from device MAC)
  val name: String,
    // label for glasses
  val connectionState: Int,
    // values can be
    //    G1ServiceState.UNINITIALIZED - the service is just setting the glasses up to connect
    //    G1ServiceState.DISCONNECTED - the glasses are not connected
    //    G1ServiceState.CONNECTING - the service is connecting to the glasses
    //    G1ServiceState.CONNECTED - the glasses are ready to use
    //    G1ServiceState.DISCONNECTING - the service is disconnecting from the glasses
    //    G1ServiceState.ERROR - an error ocurred while setting up or connecting the glasses
  val batteryPercentage: Int,
    // the percentage battery left of the side that has the least left
)
```

### 3. Scanning for Glasses

To start scanning for glasses, call 

```kotlin
client.lookForGlasses()
```

The function will scan for glasses for 15 seconds. The client.state flow will update as changes occur. 

### 4. Connecting and Disconnecting

To connect to a pair of glasses, call the suspend function

```kotlin
val success = client.connect(id)
```

in a coroutine scope, using the id of the glasses you want to connect.  
The client.state will update as changes in connection state of the glasses occur.
Similarly, to disconnect, invoke

```kotlin
client.disconnect(id)
```

### 4. Displaying Text

The basic facility to display text immediately is the suspend function

``kotlin
val success = client.displayTextPage(id, page)
``

where id is the glasses id, and page is a list of a maximum of five strings of a maximum 40-character width.
This call displays the text immediately as it is formatted, and leaves it on the display.

To stop displaying text, call

``kotlin
val success = client.stopDisplaying(id)
``

this clears the text and goes back to the previous context (screen off, dashboard).



*(more details coming soon)*

## Example
The **example** module contains a Compose application that demonstrates use of the service. 
The application can seamlessly and reliably discover, connect and disconnect, and send commands to glasses.

*(more details coming soon)*
