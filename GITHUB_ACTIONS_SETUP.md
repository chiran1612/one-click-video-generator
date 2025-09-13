# GitHub Actions CI/CD Setup Guide

This guide will help you set up the CI/CD pipeline for your Riding Roney Video Generator project.

## 🔧 Required GitHub Secrets

You need to add these secrets to your GitHub repository:

### 1. Railway Deployment Secrets

1. Go to your GitHub repository
2. Click **Settings** → **Secrets and variables** → **Actions**
3. Click **New repository secret**
4. Add these secrets:

#### `RAILWAY_TOKEN`
- **Description**: Railway authentication token
- **How to get**: 
  1. Go to [Railway.app](https://railway.app)
  2. Login to your account
  3. Go to **Account Settings** → **Tokens**
  4. Create a new token
  5. Copy the token value

#### `RAILWAY_URL`
- **Description**: Your Railway deployment URL
- **Example**: `https://riding-roney-generator-production.up.railway.app`
- **How to get**: After deploying to Railway, copy the URL from your project dashboard

## 🚀 Deployment Platforms

### Option 1: Railway (Recommended)
- **Pros**: Easy setup, automatic deployments, free tier
- **Setup**: 
  1. Connect your GitHub repository to Railway
  2. Add the secrets above
  3. Push to main branch to trigger deployment

### Option 2: Heroku
- **Pros**: Popular platform, good documentation
- **Setup**: 
  1. Create Heroku app
  2. Add `HEROKU_API_KEY` secret
  3. Add `HEROKU_APP_NAME` secret

### Option 3: AWS/GCP/Azure
- **Pros**: More control, enterprise features
- **Setup**: More complex, requires cloud provider setup

## 📋 Workflow Files Created

### 1. `ci.yml` - Continuous Integration
- **Triggers**: Push to main/develop, Pull requests
- **Actions**: 
  - Build and test
  - Code coverage
  - Upload artifacts
  - Generate reports

### 2. `cd.yml` - Continuous Deployment
- **Triggers**: Push to main branch
- **Actions**:
  - Deploy to Railway
  - Health check
  - Notify success/failure

### 3. `docker.yml` - Docker Build
- **Triggers**: Push to main, tags, pull requests
- **Actions**:
  - Build Docker image
  - Push to GitHub Container Registry
  - Multi-platform support

## 🔄 Workflow Triggers

### Automatic Triggers
- **Push to main**: Triggers CI + CD + Docker build
- **Push to develop**: Triggers CI + Docker build
- **Pull Request**: Triggers CI + Docker build
- **Tags (v*)**: Triggers Docker build with version tags

### Manual Triggers
- **Workflow Dispatch**: Manual deployment trigger
- **Repository Dispatch**: External trigger support

## 🐳 Docker Support

### Local Development
```bash
# Build and run with Docker Compose
docker-compose up --build

# Run in production mode
docker-compose --profile production up
```

### Production Deployment
```bash
# Build Docker image
docker build -t riding-roney-generator .

# Run container
docker run -p 8080:8080 riding-roney-generator
```

## 📊 Monitoring and Logs

### GitHub Actions
- View workflow runs in **Actions** tab
- Check logs for each step
- Monitor build status and deployment

### Railway
- View deployment logs in Railway dashboard
- Monitor application health
- Check resource usage

### Application Logs
- Logs saved to `./logs/` directory
- Structured logging with timestamps
- Health check endpoint: `/health`

## 🛠️ Customization

### Environment Variables
Add to your deployment platform:
```bash
SPRING_PROFILES_ACTIVE=production
SERVER_PORT=8080
```

### Build Configuration
Edit `pom.xml` for:
- Java version
- Dependencies
- Build plugins

### Docker Configuration
Edit `Dockerfile` for:
- Base image
- Port configuration
- Health checks

## 🚨 Troubleshooting

### Common Issues

1. **Build Fails**
   - Check Java version compatibility
   - Verify Maven dependencies
   - Check for syntax errors

2. **Deployment Fails**
   - Verify Railway token
   - Check Railway URL
   - Ensure health check endpoint works

3. **Docker Build Fails**
   - Check Dockerfile syntax
   - Verify base image availability
   - Check multi-platform support

### Debug Steps

1. **Check GitHub Actions logs**
2. **Verify secrets are set correctly**
3. **Test locally with Docker**
4. **Check Railway deployment logs**

## 📈 Next Steps

1. **Set up secrets** in GitHub repository
2. **Push code** to trigger first build
3. **Monitor deployment** in Railway dashboard
4. **Test the application** at your Railway URL
5. **Set up monitoring** and alerts

## 🎯 Success Criteria

- ✅ CI pipeline runs successfully
- ✅ Tests pass
- ✅ Docker image builds
- ✅ Application deploys to Railway
- ✅ Health check passes
- ✅ Application is accessible via URL

---

**Your Riding Roney Video Generator is now ready for automated CI/CD! 🚴‍♂️**
