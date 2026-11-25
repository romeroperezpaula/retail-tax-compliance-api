output "cluster_name" {
  description = "Nombre del clúster EKS"
  value       = module.eks.cluster_name
}

output "cluster_endpoint" {
  description = "Endpoint de la API de Kubernetes"
  value       = module.eks.cluster_endpoint
}

output "region" {
  description = "Región de AWS"
  value       = var.aws_region
}

output "configure_kubectl" {
  description = "Comando para configurar kubectl contra el clúster EKS"
  value       = "aws eks --region ${var.aws_region} update-kubeconfig --name ${module.eks.cluster_name}"
}