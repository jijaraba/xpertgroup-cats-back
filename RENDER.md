# Deploy to Render.com

This guide explains how to deploy the Cats API to [Render.com](https://render.com), a modern cloud platform that provides free hosting for web applications and databases.

## 🚀 Quick Deployment

### Prerequisites

1. **GitHub Repository**: Your code must be in a GitHub repository
2. **Render Account**: Create a free account at [render.com](https://render.com)
3. **TheCatAPI Key**: Get your free API key from [thecatapi.com](https://thecatapi.com)

### Option 1: Automatic Deployment (Recommended)

**Using render.yaml (Infrastructure as Code):**

1. **Push your code to GitHub** (including the `render.yaml` file)

2. **Create Render service:**
   - Go to [Render Dashboard](https://dashboard.render.com)
   - Click "New" → "Blueprint"
   - Connect your GitHub repository
   - Select the repository containing this project
   - Render will automatically detect `render.yaml` and create:
     - PostgreSQL database (`cats-postgres`)
     - Web service (`cats-api`)

3. **Set environment variables:**
   - Go to your web service in Render Dashboard
   - Navigate to "Environment" tab
   - Add: `CATS_API_KEY` = `your_api_key_from_thecatapi.com`
   - Other variables are set automatically via `render.yaml`

4. **Deploy:**
   - Render will automatically build and deploy your application
   - Your API will be available at: `https://your-service-name.onrender.com`

### Option 2: Manual Deployment

**Step-by-step manual setup:**

1. **Create PostgreSQL Database:**
   - Dashboard → "New" → "PostgreSQL"
   - Name: `cats-postgres`
   - Database Name: `cats_db`
   - User: `cats_user`
   - Plan: Free
   - Click "Create Database"

2. **Create Web Service:**
   - Dashboard → "New" → "Web Service"
   - Connect your GitHub repository
   - Name: `cats-api`
   - Environment: `Java`
   - Build Command: `./gradlew bootJar --no-daemon`
   - Start Command: `java -Dserver.port=$PORT -jar build/libs/cats-0.0.1-SNAPSHOT.jar`

3. **Configure Environment Variables:**
   ```
   DATABASE_URL                = [Auto-generated from PostgreSQL service]
   SPRING_PROFILES_ACTIVE     = production
   CATS_API_BASE_URL          = https://api.thecatapi.com/v1
   CATS_API_KEY               = your_api_key_here
   ```

4. **Deploy:**
   - Click "Create Web Service"
   - Render will clone, build, and deploy your application

## 🔧 Configuration Details

### Environment Variables

| Variable | Value | Source |
|----------|-------|--------|
| `DATABASE_URL` | Auto-generated PostgreSQL connection string | Render Database |
| `SPRING_PROFILES_ACTIVE` | `production` | Manual |
| `CATS_API_BASE_URL` | `https://api.thecatapi.com/v1` | Manual |
| `CATS_API_KEY` | Your API key from TheCatAPI | Manual |
| `PORT` | Auto-assigned by Render | Render |

### Database Configuration

- **Type**: PostgreSQL (provided by Render)
- **Free Tier**: 1 GB storage, sleeps after 90 days of inactivity
- **Connection**: Automatic via `DATABASE_URL` environment variable

### Application Profiles

The application uses different profiles:
- **Local Development**: `default` (H2 database)
- **Docker**: `docker` (H2 or PostgreSQL)
- **Render Production**: `production` (PostgreSQL)

## 🧪 Testing Your Deployment

Once deployed, test your API endpoints:

```bash
# Replace YOUR_APP_NAME with your actual Render service name
BASE_URL="https://YOUR_APP_NAME.onrender.com"

# Test endpoints
curl "$BASE_URL/breeds" | head -10
curl "$BASE_URL/breeds/abys"
curl "$BASE_URL/breeds/search?q=persian"
curl "$BASE_URL/imagesbybreedid?breed_id=abys&limit=3"
```

## 🔄 Automatic Updates

**GitHub Integration:**
- Connect your Render service to your GitHub repository
- Auto-deploy on every push to main branch
- View build logs in Render Dashboard

**Manual Deploy:**
- Use "Manual Deploy" button in Render Dashboard
- Useful for deploying specific commits or branches

## 💰 Cost Considerations

**Free Tier Includes:**
- Web Service: 750 hours/month (about 31 days)
- PostgreSQL: 1 GB storage
- Bandwidth: 100 GB/month
- Custom domains supported

**Limitations:**
- Services sleep after 15 minutes of inactivity
- Cold start time: ~30 seconds
- No persistent file storage

## 🐛 Troubleshooting

### Common Issues

**1. Build Failures:**
```bash
# Check Java version in logs
./gradlew --version

# Ensure Gradle wrapper is executable
chmod +x ./gradlew
```

**2. Database Connection:**
```bash
# Verify DATABASE_URL is set
echo $DATABASE_URL

# Check PostgreSQL service status in Dashboard
```

**3. Port Issues:**
```bash
# Ensure application uses $PORT variable
# Check application-production.properties:
server.port=${PORT:8080}
```

**4. API Key Issues:**
```bash
# Verify CATS_API_KEY is set in Environment variables
# Test API key directly: https://api.thecatapi.com/v1/breeds
```

### Logs and Monitoring

- **Build Logs**: Available during deployment
- **Application Logs**: Real-time in Render Dashboard
- **Database Logs**: In PostgreSQL service dashboard

## 🔒 Security Best Practices

1. **Environment Variables**: Never commit API keys to git
2. **Database**: Use Render's managed PostgreSQL
3. **HTTPS**: Automatic SSL/TLS certificates
4. **Non-root User**: Application runs as non-privileged user

## 📊 Performance Tips

1. **Health Checks**: Add health endpoint to prevent sleeping
2. **Connection Pooling**: Configure database connection pool
3. **Caching**: Implement Redis for caching (Render add-on)
4. **CDN**: Use Render's global CDN for static assets

## 🔗 Useful Links

- [Render Documentation](https://render.com/docs)
- [Render Spring Boot Guide](https://render.com/docs/deploy-spring-boot)
- [PostgreSQL on Render](https://render.com/docs/databases)
- [Environment Variables](https://render.com/docs/environment-variables)

## 🆘 Support

**Render Support:**
- [Help Center](https://render.com/help)
- [Community Forum](https://community.render.com)
- Email: help@render.com

**Project Support:**
- Create GitHub Issues for bugs
- Check logs in Render Dashboard
- Verify environment variables setup

---

**Deployment to Render.com is complete! 🎉**