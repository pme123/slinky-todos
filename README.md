# Todo List with Slinky
## Goals
* Having a minimal Client-Server Setup, using Scala.
* Using Slinky on the Client side.
* Having a decent UI.

`git clone https://github.com/pme123/slinky-todos.git`

## Usage in Dev
### Server Development
`sbt server/run`

This starts the Web Server on **Port 8883**.

>This copies the client assets to the classpath of the server.
> So make sure you run `build` before.
> 
> Or use the client as described in the next chapter.

### Client Development
`sbt dev`

This will watch all your changes in the client and automatically refresh your Browser Session.

Open in the Browser **http://localhost:8024**.

> WARN: You might get a CORS Exception getting the TODOs from the Server.
>
> See https://stackoverflow.com/a/38000615/2750966 for a solution.

## Production Build
`sbt release`

Creates an optimized Client bundle and adds it to the Server Library.

The whole distribution you can find here: `./server/target/universal/stage`.

You can start the server like this:

* Mac / Linux: `./server/target/universal/stage/bin/slinky-todos-server`
* Windows: `./server/target/universal/stage/bin/slinky-todos-server`

If you want to build a distribution check here for the possibilities: 
[sbt-native-packager](https://www.scala-sbt.org/sbt-native-packager/gettingstarted.html#)

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
* [http4s](https://http4s.org)
  > Typeful, functional, streaming HTTP for Scala.
