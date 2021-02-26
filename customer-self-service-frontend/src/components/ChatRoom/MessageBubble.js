// @flow

import React from "react"

export type Props = {
  message: Message,
}

export default (props: Props) => (
  <li className={`chat ${!props.message.sentByOperator ? "right" : "left"}`}>
    {props.message.content}
  </li>
)
