# Todo List with Slinky
## Goals
* Having a minimal Client-Server Setup, using Scala.
* Using Slinky on the Client side.
* Having a decent UI.


## Usage

### Client
In the sbt console:

`dev` Development with live updates of the client side.

`build` Bundle the Client.

## Thanks to
This example is more or less a mashup from different Github projects:

### Scala.js SPA-tutorial
The Communication between client and server I took from this excellent tutorial.
* see [Scala.js SPA-tutorial](https://ochrons.github.io/scalajs-spa-tutorial/en/)
* and the according Source Code: [scalajs-spa-tutorial](https://github.com/ochrons/scalajs-spa-tutorial)
### React Ant Design Todo List
I just translated the React Code from this project to Scala/Slinky. 
I made some simplifications, like skipping 'redux' and 'routing'
* See here the Demo: [react-antd-todo.netlify.app](https://react-antd-todo.netlify.app/#/)
* And the Project [react-antd-todo](https://github.com/leonardopliski/react-antd-todo)
### Scalably Slinky Demo
This project has also a Demo for Ant Design. 
So after spending some time, trying to figure it out - I found there all code snippets I needed.
* See here the Demo: [Ant Design Demo](https://scalablytyped.github.io/SlinkyDemos/antd/)
* And the Project: [SlinkyDemos](https://github.com/ScalablyTyped/SlinkyDemos)
### My Scalably Slinky example
A simple project that contains only the client side.
* see [scalably-slinky-example](https://github.com/pme123/scalably-slinky-example)

## Technologies

### Shared
* [Autowire](https://github.com/lihaoyi/autowire):
  > Autowire is a pair of macros that allows you to perform type-safe, reflection-free RPC between Scala systems.
* [BooPickle](https://boopickle.suzaku.io):
  > BooPickle is the fastest and most size efficient serialization (aka pickling) library that works on both Scala and Scala.js.

### Client
* [Slinky](https://slinky.dev)
  > Write React apps in Scala just like you would in ES6
* [Scalably Typed](https://scalablytyped.org)
  > The Javascript ecosystem for Scala.js!
  I used the facades for Ant Design
* [Ant Design](https://ant.design)
  >A design system for enterprise-level products. Create an efficient and enjoyable work experience.

### Server
* [uzhttp](https://github.com/polynote/uzhttp)
  > This (Micro-Z-HTTP, or "uzi-HTTP" if you like) is a minimal HTTP server using ZIO. 
  > It has essentially no features. 
  > You probably shouldn't use it.
