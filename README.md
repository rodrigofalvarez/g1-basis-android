# G1 Basis
Open, multipurpose infrastructure for writing Android applications that talk to the Even Realities G1 glasses.

## Core
The **core** module contains the source code to the core library. This library allows for interfacing directly with the glasses through a simple abstraction that uses modern Android and Kotlin features like coroutines and Flow. 
When using this library directly, only one application can connect to and interface with the glasses at one time. The basis core lirbary is used under the hood by the **service**.

## Service
The **service** module implements a shared Android service that multiple applications can use to interface simultaneously with the glasses.  
Applications interface with the service using the AIDL-defined generated code and a simple wrapper that exposes the service using coroutines and flow the way the Core library does.

## Example
The **example** module contains an application that demonstrates use of the service. The application can discover, connect and disconnect, and send commands to glasses.
