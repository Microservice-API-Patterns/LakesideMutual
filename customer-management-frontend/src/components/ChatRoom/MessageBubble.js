// @flow

import React from "react"

export type Props = {
  message: MessageDto,
}

export default (props: Props) => (
  <li className={`chat ${props.message.sentByOperator ? "right" : "left"}`}>
    {props.message.content}
  </li>
)
