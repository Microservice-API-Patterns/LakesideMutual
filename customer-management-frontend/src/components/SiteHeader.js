// @flow

import React from "react"
import { Header } from "semantic-ui-react"
import { Link } from "react-router-dom"

const styles = {
  header: { color: "#192a56", paddingTop: 20, marginBottom: 0 },
  subheader: {
    fontSize: 22,
    fontWeight: "lighter",
    color: "#273c75",
    margin: 0,
    padding: 0,
  },
}

export type Props = {}

export default (props: Props) => {
  return (
    <Link to="/">
      <Header
        as="h1"
        style={styles.header}
        content="Customer Management"
      />
      <p style={styles.subheader}>Lakeside Mutual</p>
    </Link>
  )
}
