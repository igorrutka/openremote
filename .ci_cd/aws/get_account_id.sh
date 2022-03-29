#!/bin/bash

# Sets ACCOUNT_ID by looking up the requested ACCOUNT_NAME

if [ -z "$ACCOUNT_NAME" ]; then
  echo "ACCOUNT_NAME must be set to use this script"
  exit 1
fi

if [[ $BASH_SOURCE = */* ]]; then
 awsDir=${BASH_SOURCE%/*}/
else
  awsDir=./
fi

# Optionally login if AWS_ENABLED != 'true'
source "${awsDir}login.sh"

ACCOUNT_ID=$(aws ssm get-parameter --name "/Account-Ids/$ACCOUNT_NAME" --query "Parameter.Value" --output text 2>/dev/null)

if [ -z "$ACCOUNT_ID" ]; then
  echo "Looking for account in organization"
  ACCOUNT_ID=$(aws organizations list-accounts --query "Accounts[?Name=='$ACCOUNT_NAME'].Id" --output text 2>/dev/null)

  if [ -n "$ACCOUNT_ID" ]; then
    echo "Storing account ID for future reference"
    aws ssm put-parameter --name "/Account-Ids/$ACCOUNT_NAME" --value "$ACCOUNT_ID" --type String &>/dev/null
  fi
fi

if [ -z "$ACCOUNT_ID" ]; then
  echo "Failed to find account ID for ACCOUNT_NAME: $ACCOUNT_NAME"
else
  echo "$ACCOUNT_ID"
fi