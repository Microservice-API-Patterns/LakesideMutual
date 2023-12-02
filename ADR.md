# Architectural Decisions in the Lakeside Mutual Scenario

## Backend Integration Styles

ADR-01:

~~~
In the context of the interface between the Policy Management Backend and the Risk Management Backend,
facing the need to integrate heterogenuous subsystems
  with different quality-of-service characteristics (e.g., uptime),
we decided for the Messaging style
  and neglected File Transfer, RPI, Shared Database
to achieve guaranteed delivery
  and decoupling in the reference, platform and time dimensions
accepting that the asynchronous communication channel and the two messaging endpoints have to be developed and managed
  (which is more involved that local calls or synchronous invocations). 
~~~

Template from ["Y-Statements: A light template for architectural decision capturing"](https://medium.com/olzzio/y-statements-10eb07b5a177).

## More ADs

Some ADRs are embedded in the source code with the help of annotations from the e-adr project. 

*to be continued*
