#!/bin/bash
arp -a | grep "b8:27" | grep -E -o "([0-9]{1,3}[\.]){3}[0-9]{1,3}"
