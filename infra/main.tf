# Main Terraform configuration file
# This file contains the primary infrastructure resources

terraform {
  required_version = ">= 1.0"
  
  required_providers {
    # Add providers as needed for your infrastructure
    # Example:
    # aws = {
    #   source  = "hashicorp/aws"
    #   version = "~> 5.0"
    # }
  }
}

# Configure providers here
# Example:
# provider "aws" {
#   region = var.aws_region
# }

# Define your infrastructure resources here
# Example:
# resource "aws_instance" "example" {
#   ami           = var.ami_id
#   instance_type = var.instance_type
#   
#   tags = {
#     Name = "example-instance"
#   }
# }